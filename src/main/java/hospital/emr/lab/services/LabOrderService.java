package hospital.emr.lab.services;

import hospital.emr.lab.dtos.*;
import hospital.emr.lab.entities.*;
import hospital.emr.lab.enums.*;
import hospital.emr.lab.repos.*;
import hospital.emr.notification.service.DepartmentNotificationService;
import hospital.emr.security.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LabOrderService {

    private final LabOrderRepository orderRepository;
    private final LabOrderItemRepository orderItemRepository;
    private final LabSpecimenRepository specimenRepository;
    private final LabAnalyteResultRepository analyteResultRepository;
    private final LabOrderAuditRepository auditRepository;
    private final LabTestRepository labTestRepository;
    private final DepartmentNotificationService notificationService;

    private static final DateTimeFormatter ACCESSION_DATE = DateTimeFormatter.ofPattern("yyyyMMdd");

    // ------------------------------------------------------------------
    // Commands
    // ------------------------------------------------------------------

    @Transactional
    public LabOrderDTO createOrder(CreateLabOrderRequest request) {
        LabOrder order = new LabOrder();
        order.setPatientId(request.getPatientId());
        order.setVisitId(request.getVisitId());
        order.setMedicalHistoryId(request.getMedicalHistoryId());
        order.setPriority(request.getPriority() != null ? request.getPriority() : LabPriority.ROUTINE);
        order.setClinicalNotes(request.getClinicalNotes());
        order.setOrderedById(SecurityUtils.currentUserId());
        order.setOrderedByName(SecurityUtils.currentUserName());
        order.setStatus(LabOrderStatus.ORDERED);

        for (CreateLabOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            LabTest test = labTestRepository.findById(itemRequest.getTestId())
                    .orElseThrow(() -> new EntityNotFoundException("Lab test not found with id: " + itemRequest.getTestId()));
            LabOrderItem item = new LabOrderItem();
            item.setTestId(test.getId());
            item.setTestName(test.getName());
            item.setSampleType(test.getSampleType());
            item.setPrice(test.getPrice());
            item.setStatus(LabOrderStatus.ORDERED);
            item.setNotes(itemRequest.getNotes());
            order.addItem(item);
        }

        LabOrder saved = orderRepository.save(order);
        recordAudit(saved.getId(), "ORDER_CREATED", null, saved.getStatus(),
                saved.getItems().size() + " test(s) ordered");
        log.info("Created lab order {} for patient {}", saved.getId(), saved.getPatientId());
        return toDTO(saved);
    }

    @Transactional
    public LabOrderDTO collectSpecimen(Long orderId, CollectSpecimenRequest request) {
        LabOrder order = requireOrder(orderId);
        LabOrderStatus previous = order.getStatus();
        requireTransition(previous, LabOrderStatus.COLLECTED);

        LabSpecimen specimen = new LabSpecimen();
        specimen.setLabOrder(order);
        specimen.setSampleType(request.getSampleType() != null ? request.getSampleType() : SampleType.OTHER);
        specimen.setAccessionNumber(request.getAccessionNumber() != null
                ? request.getAccessionNumber() : generateAccessionNumber());
        specimen.setStatus(SpecimenStatus.COLLECTED);
        specimen.setCollectedById(SecurityUtils.currentUserId());
        specimen.setCollectedByName(SecurityUtils.currentUserName());
        specimen.setCollectedAt(LocalDateTime.now());
        specimen.setNotes(request.getNotes());
        LabSpecimen savedSpecimen = specimenRepository.save(specimen);

        List<LabOrderItem> items = order.getItems();
        for (LabOrderItem item : items) {
            boolean applies = request.getOrderItemIds() == null
                    || request.getOrderItemIds().isEmpty()
                    || request.getOrderItemIds().contains(item.getId())
                    || (request.getSampleType() != null && item.getSampleType() == request.getSampleType());
            if (applies) {
                item.setSpecimen(savedSpecimen);
                item.setStatus(LabOrderStatus.COLLECTED);
            }
        }

        order.setStatus(LabOrderStatus.COLLECTED);
        LabOrder saved = orderRepository.save(order);
        recordAudit(saved.getId(), "SPECIMEN_COLLECTED", previous, saved.getStatus(),
                "Accession " + savedSpecimen.getAccessionNumber());
        return toDTO(saved);
    }

    @Transactional
    public LabOrderDTO receiveSpecimen(Long orderId, Long specimenId, String notes) {
        LabOrder order = requireOrder(orderId);
        LabSpecimen specimen = specimenRepository.findById(specimenId)
                .orElseThrow(() -> new EntityNotFoundException("Specimen not found with id: " + specimenId));
        if (!specimen.getLabOrder().getId().equals(orderId)) {
            throw new IllegalArgumentException("Specimen does not belong to order " + orderId);
        }
        specimen.setStatus(SpecimenStatus.RECEIVED);
        specimen.setReceivedById(SecurityUtils.currentUserId());
        specimen.setReceivedByName(SecurityUtils.currentUserName());
        specimen.setReceivedAt(LocalDateTime.now());
        if (notes != null) {
            specimen.setNotes(notes);
        }
        specimenRepository.save(specimen);

        LabOrderStatus previous = order.getStatus();
        for (LabOrderItem item : order.getItems()) {
            if (item.getSpecimen() != null && item.getSpecimen().getId().equals(specimenId)
                    && item.getStatus() == LabOrderStatus.COLLECTED) {
                item.setStatus(LabOrderStatus.IN_LAB);
                item.setPerformedById(SecurityUtils.currentUserId());
                item.setPerformedByName(SecurityUtils.currentUserName());
                item.setPerformedAt(LocalDateTime.now());
            }
        }
        if (previous == LabOrderStatus.COLLECTED) {
            order.setStatus(LabOrderStatus.IN_LAB);
        }
        LabOrder saved = orderRepository.save(order);
        recordAudit(saved.getId(), "SPECIMEN_RECEIVED", previous, saved.getStatus(),
                "Accession " + specimen.getAccessionNumber());
        return toDTO(saved);
    }

    @Transactional
    public LabOrderDTO enterResults(Long orderId, EnterResultsRequest request) {
        LabOrder order = requireOrder(orderId);
        LabOrderStatus previous = order.getStatus();
        if (previous != LabOrderStatus.IN_LAB && previous != LabOrderStatus.RESULTED) {
            throw new IllegalStateException(
                    "Results can only be entered once the order is in the lab (current status: " + previous + ")");
        }

        for (EnterResultsRequest.ItemResult itemResult : request.getItems()) {
            LabOrderItem item = order.getItems().stream()
                    .filter(i -> i.getId().equals(itemResult.getOrderItemId()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Order item " + itemResult.getOrderItemId() + " not found on order " + orderId));

            item.getAnalyteResults().clear();
            if (itemResult.getAnalytes() != null) {
                for (EnterResultsRequest.AnalyteInput input : itemResult.getAnalytes()) {
                    LabAnalyteResult result = new LabAnalyteResult();
                    result.setAnalyteName(input.getAnalyteName() != null ? input.getAnalyteName() : item.getTestName());
                    result.setValueNumeric(input.getValueNumeric());
                    result.setValueText(input.getValueText());
                    result.setUnit(input.getUnit());
                    result.setRefLow(input.getRefLow());
                    result.setRefHigh(input.getRefHigh());
                    result.setCriticalLow(input.getCriticalLow());
                    result.setCriticalHigh(input.getCriticalHigh());
                    result.setMethod(input.getMethod());
                    result.setComment(input.getComment());
                    result.setFlag(computeFlag(input));
                    item.addAnalyteResult(result);
                }
            }
            if (itemResult.getNotes() != null) {
                item.setNotes(itemResult.getNotes());
            }
            item.setStatus(LabOrderStatus.RESULTED);
            item.setResultedById(SecurityUtils.currentUserId());
            item.setResultedByName(SecurityUtils.currentUserName());
            item.setResultedAt(LocalDateTime.now());
        }

        boolean allResulted = order.getItems().stream()
                .allMatch(i -> i.getStatus() == LabOrderStatus.RESULTED
                        || i.getStatus() == LabOrderStatus.VERIFIED);
        if (allResulted) {
            if (order.getStatus() != LabOrderStatus.RESULTED) {
                requireTransition(order.getStatus(), LabOrderStatus.RESULTED);
                order.setStatus(LabOrderStatus.RESULTED);
            }
        } else if (order.getStatus() == LabOrderStatus.RESULTED) {
            order.setStatus(LabOrderStatus.IN_LAB);
        }

        LabOrder saved = orderRepository.save(order);
        recordAudit(saved.getId(), "RESULTS_ENTERED", previous, saved.getStatus(),
                request.getItems().size() + " item(s) resulted");
        alertOnCriticalResults(saved);
        return toDTO(saved);
    }

    @Transactional
    public LabOrderDTO verifyOrder(Long orderId, String notes) {
        LabOrder order = requireOrder(orderId);
        LabOrderStatus previous = order.getStatus();
        requireTransition(previous, LabOrderStatus.VERIFIED);
        order.setStatus(LabOrderStatus.VERIFIED);
        for (LabOrderItem item : order.getItems()) {
            if (item.getStatus() == LabOrderStatus.RESULTED) {
                item.setStatus(LabOrderStatus.VERIFIED);
            }
        }
        LabOrder saved = orderRepository.save(order);
        recordAudit(saved.getId(), "RESULTS_VERIFIED", previous, saved.getStatus(), notes);
        return toDTO(saved);
    }

    @Transactional
    public LabOrderDTO cancelOrder(Long orderId, String reason) {
        LabOrder order = requireOrder(orderId);
        LabOrderStatus previous = order.getStatus();
        requireTransition(previous, LabOrderStatus.CANCELLED);
        order.setStatus(LabOrderStatus.CANCELLED);
        for (LabOrderItem item : order.getItems()) {
            if (item.getStatus() != LabOrderStatus.VERIFIED) {
                item.setStatus(LabOrderStatus.CANCELLED);
            }
        }
        LabOrder saved = orderRepository.save(order);
        recordAudit(saved.getId(), "ORDER_CANCELLED", previous, saved.getStatus(), reason);
        return toDTO(saved);
    }

    @Transactional
    public LabOrderDTO acknowledgeCritical(Long orderId, String notes) {
        LabOrder order = requireOrder(orderId);
        if (order.getStatus() != LabOrderStatus.RESULTED && order.getStatus() != LabOrderStatus.VERIFIED) {
            throw new IllegalStateException("Critical values can only be acknowledged once results are entered");
        }
        boolean hasCritical = order.getItems().stream()
                .flatMap(item -> item.getAnalyteResults().stream())
                .anyMatch(result -> result.getFlag() == ResultFlag.CRITICAL);
        if (!hasCritical) {
            throw new IllegalStateException("This order has no critical results to acknowledge");
        }
        if (Boolean.TRUE.equals(order.getCriticalAcknowledged())) {
            return toDTO(order);
        }
        order.setCriticalAcknowledged(true);
        order.setCriticalAcknowledgedById(SecurityUtils.currentUserId());
        order.setCriticalAcknowledgedByName(SecurityUtils.currentUserName());
        order.setCriticalAcknowledgedAt(LocalDateTime.now());
        LabOrder saved = orderRepository.save(order);
        recordAudit(saved.getId(), "CRITICAL_ACKNOWLEDGED", saved.getStatus(), saved.getStatus(), notes);
        return toDTO(saved);
    }

    // ------------------------------------------------------------------
    // Queries
    // ------------------------------------------------------------------

    @Transactional(readOnly = true)
    public LabOrderDTO getOrder(Long id) {
        return toDTO(requireOrder(id));
    }

    @Transactional(readOnly = true)
    public List<LabOrderAuditDTO> getAudit(Long orderId) {
        return auditRepository.findByOrderIdOrderByTimestampAsc(orderId).stream()
                .map(this::toAuditDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<LabOrderDTO> listOrders(String status, Long patientId, Long visitId, String q, int page, int size) {
        LabOrderStatus statusFilter = parseStatus(status);
        Specification<LabOrder> spec = (root, query, cb) -> cb.conjunction();
        if (statusFilter != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), statusFilter));
        }
        if (patientId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("patientId"), patientId));
        }
        if (visitId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("visitId"), visitId));
        }
        if (q != null && !q.isBlank()) {
            String like = "%" + q.toLowerCase(Locale.ROOT) + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("orderedByName")), like),
                    cb.like(cb.lower(root.get("clinicalNotes")), like)));
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return orderRepository.findAll(spec, pageable).map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public List<LabOrderDTO> getOrdersByPatient(Long patientId) {
        return orderRepository.findByPatientIdOrderByCreatedAtDesc(patientId).stream()
                .map(this::toDTO)
                .toList();
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private LabOrder requireOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lab order not found with id: " + id));
    }

    private LabOrderStatus parseStatus(String status) {
        if (status == null || status.isBlank() || "ALL".equalsIgnoreCase(status)) {
            return null;
        }
        try {
            return LabOrderStatus.valueOf(status.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown lab order status: " + status);
        }
    }

    private void requireTransition(LabOrderStatus from, LabOrderStatus to) {
        if (from == null || !from.canTransitionTo(to)) {
            throw new IllegalStateException("Invalid lab order transition from " + from + " to " + to);
        }
    }

    private String generateAccessionNumber() {
        String candidate;
        do {
            candidate = "LAB-" + LocalDateTime.now().format(ACCESSION_DATE) + "-"
                    + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
        } while (specimenRepository.existsByAccessionNumber(candidate));
        return candidate;
    }

    private ResultFlag computeFlag(EnterResultsRequest.AnalyteInput input) {
        BigDecimal value = input.getValueNumeric();
        if (value == null) {
            return input.getValueText() != null && !input.getValueText().isBlank()
                    ? ResultFlag.ABNORMAL : ResultFlag.UNKNOWN;
        }
        if (input.getCriticalLow() != null && value.compareTo(input.getCriticalLow()) < 0) {
            return ResultFlag.CRITICAL;
        }
        if (input.getCriticalHigh() != null && value.compareTo(input.getCriticalHigh()) > 0) {
            return ResultFlag.CRITICAL;
        }
        if (input.getRefLow() != null && value.compareTo(input.getRefLow()) < 0) {
            return ResultFlag.LOW;
        }
        if (input.getRefHigh() != null && value.compareTo(input.getRefHigh()) > 0) {
            return ResultFlag.HIGH;
        }
        if (input.getRefLow() != null || input.getRefHigh() != null) {
            return ResultFlag.NORMAL;
        }
        return ResultFlag.UNKNOWN;
    }

    private void recordAudit(Long orderId, String action, LabOrderStatus from, LabOrderStatus to, String notes) {
        LabOrderAudit audit = new LabOrderAudit();
        audit.setOrderId(orderId);
        audit.setAction(action);
        audit.setFromStatus(from);
        audit.setToStatus(to);
        audit.setActorId(SecurityUtils.currentUserId());
        audit.setActorName(SecurityUtils.currentUserName());
        audit.setNotes(notes);
        auditRepository.save(audit);
    }

    private void alertOnCriticalResults(LabOrder order) {
        List<String> critical = order.getItems().stream()
                .flatMap(item -> item.getAnalyteResults().stream())
                .filter(result -> result.getFlag() == ResultFlag.CRITICAL)
                .map(result -> result.getAnalyteName() + " "
                        + (result.getValueNumeric() != null ? result.getValueNumeric() : result.getValueText()))
                .toList();
        if (critical.isEmpty()) {
            return;
        }
        String message = "Order #" + order.getId() + " (patient #" + order.getPatientId()
                + ") has critical result(s): " + String.join(", ", critical);
        try {
            notificationService.sendDepartmentNotification(
                    "laboratory", "doctor", "CRITICAL_RESULT",
                    "Critical Lab Result", message, "URGENT",
                    "{\"orderId\":" + order.getId() + ",\"patientId\":" + order.getPatientId() + "}");
        } catch (Exception ex) {
            log.warn("Failed to dispatch critical result alert for order {}: {}", order.getId(), ex.getMessage());
        }
    }

    private LabOrderDTO toDTO(LabOrder order) {
        LabOrderDTO dto = new LabOrderDTO();
        dto.setId(order.getId());
        dto.setPatientId(order.getPatientId());
        dto.setVisitId(order.getVisitId());
        dto.setMedicalHistoryId(order.getMedicalHistoryId());
        dto.setStatus(order.getStatus());
        dto.setPriority(order.getPriority());
        dto.setClinicalNotes(order.getClinicalNotes());
        dto.setOrderedById(order.getOrderedById());
        dto.setOrderedByName(order.getOrderedByName());
        dto.setCriticalAcknowledged(order.getCriticalAcknowledged());
        dto.setCriticalAcknowledgedById(order.getCriticalAcknowledgedById());
        dto.setCriticalAcknowledgedByName(order.getCriticalAcknowledgedByName());
        dto.setCriticalAcknowledgedAt(order.getCriticalAcknowledgedAt());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setItems(order.getItems().stream().map(this::toItemDTO).toList());
        dto.setSpecimens(order.getSpecimens().stream().map(this::toSpecimenDTO).toList());
        dto.setAuditTrail(auditRepository.findByOrderIdOrderByTimestampAsc(order.getId()).stream()
                .map(this::toAuditDTO).toList());
        return dto;
    }

    private LabOrderItemDTO toItemDTO(LabOrderItem item) {
        LabOrderItemDTO dto = new LabOrderItemDTO();
        dto.setId(item.getId());
        dto.setTestId(item.getTestId());
        dto.setTestName(item.getTestName());
        dto.setSampleType(item.getSampleType());
        dto.setPrice(item.getPrice());
        dto.setStatus(item.getStatus());
        dto.setSpecimenId(item.getSpecimen() != null ? item.getSpecimen().getId() : null);
        dto.setPerformedByName(item.getPerformedByName());
        dto.setPerformedAt(item.getPerformedAt());
        dto.setResultedByName(item.getResultedByName());
        dto.setResultedAt(item.getResultedAt());
        dto.setNotes(item.getNotes());
        dto.setAnalyteResults(item.getAnalyteResults().stream().map(this::toAnalyteDTO).toList());
        return dto;
    }

    private LabAnalyteResultDTO toAnalyteDTO(LabAnalyteResult result) {
        LabAnalyteResultDTO dto = new LabAnalyteResultDTO();
        dto.setId(result.getId());
        dto.setAnalyteName(result.getAnalyteName());
        dto.setValueNumeric(result.getValueNumeric());
        dto.setValueText(result.getValueText());
        dto.setUnit(result.getUnit());
        dto.setRefLow(result.getRefLow());
        dto.setRefHigh(result.getRefHigh());
        dto.setCriticalLow(result.getCriticalLow());
        dto.setCriticalHigh(result.getCriticalHigh());
        dto.setFlag(result.getFlag());
        dto.setMethod(result.getMethod());
        dto.setComment(result.getComment());
        return dto;
    }

    private LabSpecimenDTO toSpecimenDTO(LabSpecimen specimen) {
        LabSpecimenDTO dto = new LabSpecimenDTO();
        dto.setId(specimen.getId());
        dto.setAccessionNumber(specimen.getAccessionNumber());
        dto.setSampleType(specimen.getSampleType());
        dto.setStatus(specimen.getStatus());
        dto.setCollectedByName(specimen.getCollectedByName());
        dto.setCollectedAt(specimen.getCollectedAt());
        dto.setReceivedByName(specimen.getReceivedByName());
        dto.setReceivedAt(specimen.getReceivedAt());
        dto.setRejectedReason(specimen.getRejectedReason());
        dto.setNotes(specimen.getNotes());
        return dto;
    }

    private LabOrderAuditDTO toAuditDTO(LabOrderAudit audit) {
        LabOrderAuditDTO dto = new LabOrderAuditDTO();
        dto.setId(audit.getId());
        dto.setAction(audit.getAction());
        dto.setFromStatus(audit.getFromStatus());
        dto.setToStatus(audit.getToStatus());
        dto.setActorId(audit.getActorId());
        dto.setActorName(audit.getActorName());
        dto.setNotes(audit.getNotes());
        dto.setTimestamp(audit.getTimestamp());
        return dto;
    }
}
