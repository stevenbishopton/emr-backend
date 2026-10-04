package hospital.emr.lab.controllers;

import hospital.emr.lab.dtos.*;
import hospital.emr.lab.services.LabOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Order -> specimen -> result -> verify workflow for the laboratory.
 * This is the canonical lab API; legacy /lab-test-requests endpoints remain
 * for backward compatibility during migration.
 */
@RestController
@RequestMapping("/emr/lab/orders")
@RequiredArgsConstructor
@Slf4j
public class LabOrderController {

    private final LabOrderService labOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_LAB_PERSONNEL', 'ROLE_NURSE')")
    public ResponseEntity<LabOrderDTO> createOrder(@Valid @RequestBody CreateLabOrderRequest request) {
        return new ResponseEntity<>(labOrderService.createOrder(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabOrderDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(labOrderService.getOrder(id));
    }

    @GetMapping
    public ResponseEntity<Page<LabOrderDTO>> listOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long visitId,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(labOrderService.listOrders(status, patientId, visitId, q, page, size));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabOrderDTO>> getOrdersByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(labOrderService.getOrdersByPatient(patientId));
    }

    @GetMapping("/{id}/audit")
    public ResponseEntity<List<LabOrderAuditDTO>> getAudit(@PathVariable Long id) {
        return ResponseEntity.ok(labOrderService.getAudit(id));
    }

    @PostMapping("/{id}/specimens/collect")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LAB_PERSONNEL', 'ROLE_NURSE')")
    public ResponseEntity<LabOrderDTO> collectSpecimen(
            @PathVariable Long id,
            @RequestBody CollectSpecimenRequest request) {
        return ResponseEntity.ok(labOrderService.collectSpecimen(id, request));
    }

    @PostMapping("/{id}/specimens/{specimenId}/receive")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LAB_PERSONNEL')")
    public ResponseEntity<LabOrderDTO> receiveSpecimen(
            @PathVariable Long id,
            @PathVariable Long specimenId,
            @RequestBody(required = false) OrderActionRequest request) {
        String notes = request != null ? request.getNotes() : null;
        return ResponseEntity.ok(labOrderService.receiveSpecimen(id, specimenId, notes));
    }

    @PostMapping("/{id}/results")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_LAB_PERSONNEL')")
    public ResponseEntity<LabOrderDTO> enterResults(
            @PathVariable Long id,
            @Valid @RequestBody EnterResultsRequest request) {
        return ResponseEntity.ok(labOrderService.enterResults(id, request));
    }

    @PostMapping("/{id}/verify")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATHOLOGIST')")
    public ResponseEntity<LabOrderDTO> verifyOrder(
            @PathVariable Long id,
            @RequestBody(required = false) OrderActionRequest request) {
        String notes = request != null ? request.getNotes() : null;
        return ResponseEntity.ok(labOrderService.verifyOrder(id, notes));
    }

    @PostMapping("/{id}/acknowledge-critical")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PATHOLOGIST', 'ROLE_DOCTOR', 'ROLE_LAB_PERSONNEL', 'ROLE_NURSE')")
    public ResponseEntity<LabOrderDTO> acknowledgeCritical(
            @PathVariable Long id,
            @RequestBody(required = false) OrderActionRequest request) {
        String notes = request != null ? request.getNotes() : null;
        return ResponseEntity.ok(labOrderService.acknowledgeCritical(id, notes));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_LAB_PERSONNEL')")
    public ResponseEntity<LabOrderDTO> cancelOrder(
            @PathVariable Long id,
            @RequestBody(required = false) OrderActionRequest request) {
        String reason = request != null ? request.getReason() : null;
        return ResponseEntity.ok(labOrderService.cancelOrder(id, reason));
    }
}
