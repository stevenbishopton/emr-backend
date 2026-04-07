package hospital.emr.reception.services;

import hospital.emr.common.entities.Department;
import hospital.emr.common.exceptions.DepartmentNotFoundException;
import hospital.emr.common.repos.DepartmentRepository;
import hospital.emr.notification.service.DepartmentNotificationService;
import hospital.emr.reception.dtos.QueueFilterRequest;
import hospital.emr.reception.dtos.VisitDepartmentDTO;
import hospital.emr.reception.entities.Visit;
import hospital.emr.reception.entities.VisitDepartment;
import hospital.emr.reception.entities.VisitDepartmentId;
import hospital.emr.reception.enums.VisitStatus;
import hospital.emr.reception.exceptions.VisitNotFoundException;
import hospital.emr.reception.mappers.VisitDepartmentMapper;
import hospital.emr.reception.repos.VisitDepartmentRepository;
import hospital.emr.reception.repos.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitDepartmentService {

    private final VisitRepository visitRepository;
    private final DepartmentRepository departmentRepository;
    private final VisitDepartmentRepository visitDepartmentRepository;
    private final VisitDepartmentMapper visitDepartmentMapper;
    private final DepartmentNotificationService departmentNotificationService;

    @Transactional
    public VisitDepartmentDTO sendToDepartment(Long visitId, Long departmentId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found with ID: " + visitId));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with ID: " + departmentId));

        VisitDepartmentId id = new VisitDepartmentId(visitId, departmentId);

        String toDepartment = department.getName();
        String fromDepartment = visitDepartmentRepository.findByIdVisitId(visitId).stream()
                .filter(vd -> vd.getStatus() == VisitStatus.IN_QUEUE)
                .map(VisitDepartment::getDepartment)
                .filter(d -> d != null && d.getId() != null && !d.getId().equals(departmentId))
                .map(Department::getName)
                .filter(name -> name != null && !name.isBlank())
                .findFirst()
                .orElse("reception");

        Optional<VisitDepartment> existingOpt = visitDepartmentRepository.findById(id);

        if (existingOpt.isPresent()) {
            VisitDepartment existing = existingOpt.get();
            // If the existing mapping is not IN_QUEUE, bring it back to IN_QUEUE (optional)
            if (existing.getStatus() != VisitStatus.IN_QUEUE) {
                existing.setStatus(VisitStatus.IN_QUEUE);
                existing.setAssignedAt(LocalDateTime.now());
                visitDepartmentRepository.save(existing);

                String metadata = String.format(
                        "{\"visitId\":%d,\"fromDepartment\":\"%s\",\"toDepartment\":\"%s\"}",
                        visitId,
                        fromDepartment,
                        toDepartment
                );

                if (toDepartment != null && !toDepartment.isBlank()) {
                    departmentNotificationService.sendDepartmentNotification(
                            fromDepartment,
                            toDepartment,
                            "QUEUE_TRANSFER",
                            "New Patient In Queue",
                            "A new patient has been added to your queue.",
                            "HIGH",
                            metadata
                    );
                }

                if (fromDepartment != null && !fromDepartment.isBlank()) {
                    departmentNotificationService.sendDepartmentNotification(
                            fromDepartment,
                            fromDepartment,
                            "QUEUE_TRANSFER",
                            "Patient Sent",
                            "A patient was sent to " + toDepartment + ".",
                            "LOW",
                            metadata
                    );
                }
            }
            return visitDepartmentMapper.toDto(existing);
        }

        VisitDepartment vd = new VisitDepartment();
        vd.setId(id);
        vd.setVisit(visit);
        vd.setDepartment(department);
        vd.setStatus(VisitStatus.IN_QUEUE);
        vd.setAssignedAt(LocalDateTime.now());

        VisitDepartment saved = visitDepartmentRepository.save(vd);

        String metadata = String.format(
                "{\"visitId\":%d,\"fromDepartment\":\"%s\",\"toDepartment\":\"%s\"}",
                visitId,
                fromDepartment,
                toDepartment
        );

        if (toDepartment != null && !toDepartment.isBlank()) {
            departmentNotificationService.sendDepartmentNotification(
                    fromDepartment,
                    toDepartment,
                    "QUEUE_TRANSFER",
                    "New Patient In Queue",
                    "A new patient has been added to your queue.",
                    "HIGH",
                    metadata
            );
        }

        if (fromDepartment != null && !fromDepartment.isBlank()) {
            departmentNotificationService.sendDepartmentNotification(
                    fromDepartment,
                    fromDepartment,
                    "QUEUE_TRANSFER",
                    "Patient Sent",
                    "A patient was sent to " + toDepartment + ".",
                    "LOW",
                    metadata
            );
        }
        return visitDepartmentMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<VisitDepartmentDTO> findDepartmentsByVisitId(Long visitId) {
        return visitDepartmentRepository.findByIdVisitId(visitId).stream()
                .map(visitDepartmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitDepartmentDTO> findVisitsByDepartmentId(Long departmentId) {
        return visitDepartmentRepository.findByIdDepartmentId(departmentId).stream()
                .map(visitDepartmentMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public VisitDepartmentDTO markDepartmentVisitAsAdmitted(Long visitId, Long departmentId) {
        VisitDepartmentId id = new VisitDepartmentId(visitId, departmentId);

        VisitDepartment visitDepartment = visitDepartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "VisitDepartment not found for visitId: " + visitId + " and departmentId: " + departmentId
                ));

        // Update the department visit status to COMPLETED
        visitDepartment.setStatus(VisitStatus.ADMITTED);

        VisitDepartment saved = visitDepartmentRepository.save(visitDepartment);

        Department dept = visitDepartment.getDepartment();
        String deptName = dept != null ? dept.getName() : null;
        if (deptName != null && !deptName.isBlank()) {
            String metadata = String.format(
                    "{\"visitId\":%d,\"department\":\"%s\",\"status\":\"COMPLETED\"}",
                    visitId,
                    deptName
            );
            departmentNotificationService.sendDepartmentNotification(
                    deptName,
                    deptName,
                    "QUEUE_UPDATE",
                    "Visit Completed",
                    "A visit was marked as admitted.",
                    "MEDIUM",
                    metadata
            );
        }

        return visitDepartmentMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<VisitDepartmentDTO> findDepartmentQueue(Long deptId) {
        return visitDepartmentRepository
                .findByDepartmentIdAndStatus(deptId, VisitStatus.IN_QUEUE)
                .stream()
                .map(visitDepartmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitDepartmentDTO> findDepartmentQueueWithFilters(Long deptId, QueueFilterRequest filters) {
        VisitStatus status = filters.getStatus();
        LocalDate date = filters.getDate();

        // Handle "today only" filter
        if (Boolean.TRUE.equals(filters.getTodayOnly())) {
            date = LocalDate.now();
        }

        List<VisitDepartment> visitDepartments;

        if (date != null) {
            // Convert LocalDate to LocalDateTime range
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            // Check if we have both filters or just date
            if (status != null) {
                // Both status and date filters
                visitDepartments = visitDepartmentRepository
                        .findDepartmentQueueWithStatusAndDateFilter(deptId, status, startOfDay, endOfDay);
            } else {
                // Only date filter
                visitDepartments = visitDepartmentRepository
                        .findDepartmentQueueWithDateFilter(deptId, startOfDay, endOfDay);
            }
        } else {
            // No date filter, check if we have status filter
            if (status != null) {
                // Only status filter
                visitDepartments = visitDepartmentRepository
                        .findDepartmentQueueWithStatusFilter(deptId, status);
            } else {
                // No filters at all - return all visits for the department
                visitDepartments = visitDepartmentRepository.findByIdDepartmentId(deptId);
            }
        }

        return visitDepartments.stream()
                .map(visitDepartmentMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public VisitDepartmentDTO markDepartmentVisitAsCompleted(Long visitId, Long departmentId) {
        VisitDepartmentId id = new VisitDepartmentId(visitId, departmentId);

        VisitDepartment visitDepartment = visitDepartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "VisitDepartment not found for visitId: " + visitId + " and departmentId: " + departmentId
                ));

        // Update the department visit status to COMPLETED
        visitDepartment.setStatus(VisitStatus.COMPLETED);

        VisitDepartment saved = visitDepartmentRepository.save(visitDepartment);

        Department dept = visitDepartment.getDepartment();
        String deptName = dept != null ? dept.getName() : null;
        if (deptName != null && !deptName.isBlank()) {
            String metadata = String.format(
                    "{\"visitId\":%d,\"department\":\"%s\",\"status\":\"COMPLETED\"}",
                    visitId,
                    deptName
            );
            departmentNotificationService.sendDepartmentNotification(
                    deptName,
                    deptName,
                    "QUEUE_UPDATE",
                    "Visit Completed",
                    "A visit was marked as completed.",
                    "MEDIUM",
                    metadata
            );
        }

        return visitDepartmentMapper.toDto(saved);
    }
    @Transactional(readOnly = true)
    public List<VisitDepartmentDTO> findDepartmentQueueServiceFilter(Long deptId, QueueFilterRequest filters) {
        List<VisitDepartment> allDeptVisits = visitDepartmentRepository.findByIdDepartmentId(deptId);

        return allDeptVisits.stream()
                .filter(vd -> filters.getStatus() == null || vd.getStatus() == filters.getStatus())
                .filter(vd -> {
                    if (filters.getDate() == null && !Boolean.TRUE.equals(filters.getTodayOnly())) {
                        return true;
                    }
                    LocalDate filterDate = Boolean.TRUE.equals(filters.getTodayOnly())
                            ? LocalDate.now()
                            : filters.getDate();
                    return vd.getAssignedAt().toLocalDate().equals(filterDate);
                })
                .sorted(Comparator.comparing(VisitDepartment::getAssignedAt))
                .map(visitDepartmentMapper::toDto)
                .collect(Collectors.toList());
    }

    private boolean visitDepartmentRepositorySupportsFilterQuery() {
        try {
            // Check if the repository has the filter query method
            // This is a simple check - you might need to adjust based on your actual repository
            visitDepartmentRepository.getClass().getMethod(
                    "findDepartmentQueueWithFilters",
                    Long.class, VisitStatus.class, LocalDate.class
            );
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}