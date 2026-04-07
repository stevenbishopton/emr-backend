package hospital.emr.radiograph.controllers;

import hospital.emr.radiograph.dtos.RadiographDTO;
import hospital.emr.radiograph.dtos.RadiographHistoryDTO;
import hospital.emr.radiograph.enums.RadiographStatus;
import hospital.emr.radiograph.services.RadiographService;
import hospital.emr.radiograph.services.RadiographHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/emr/radiographs")
@RequiredArgsConstructor
@Slf4j
public class RadiographController {

    private final RadiographService radiographService;
    private final RadiographHistoryService historyService;

    @PostMapping
    public ResponseEntity<RadiographDTO> createRadiograph(@Valid @RequestBody RadiographDTO radiographDTO) {
        log.info("Creating radiograph: {}", radiographDTO.getRadiographName());
        RadiographDTO createdRadiograph = radiographService.createRadiograph(radiographDTO);
        return new ResponseEntity<>(createdRadiograph, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RadiographDTO> updateRadiograph(
            @PathVariable Long id,
            @Valid @RequestBody RadiographDTO radiographDTO) {
        log.info("Updating radiograph: {}", id);
        RadiographDTO updatedRadiograph = radiographService.updateRadiograph(id, radiographDTO);
        return ResponseEntity.ok(updatedRadiograph);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RadiographDTO> getRadiographById(@PathVariable Long id) {
        log.debug("Fetching radiograph: {}", id);
        RadiographDTO radiographDTO = radiographService.getRadiographById(id);
        return ResponseEntity.ok(radiographDTO);
    }

    @GetMapping
    public ResponseEntity<List<RadiographDTO>> getAllRadiographs() {
        log.debug("Fetching all radiographs");
        List<RadiographDTO> radiographs = radiographService.getAllRadiographs();
        return ResponseEntity.ok(radiographs);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<RadiographDTO>> getRadiographsByPatientId(@PathVariable Long patientId) {
        log.debug("Fetching radiographs for patient: {}", patientId);
        List<RadiographDTO> radiographs = radiographService.getRadiographsByPatientId(patientId);
        return ResponseEntity.ok(radiographs);
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<RadiographDTO>> getRadiographsByVisitId(@PathVariable Long visitId) {
        log.debug("Fetching radiographs for visit: {}", visitId);
        List<RadiographDTO> radiographs = radiographService.getRadiographsByVisitId(visitId);
        return ResponseEntity.ok(radiographs);
    }

    @GetMapping("/medical-history/{medicalHistoryId}")
    public ResponseEntity<List<RadiographDTO>> getRadiographsByMedicalHistoryId(
            @PathVariable Long medicalHistoryId) {
        log.debug("Fetching radiographs for medical history: {}", medicalHistoryId);
        List<RadiographDTO> radiographs = radiographService.getRadiographsByMedicalHistoryId(medicalHistoryId);
        return ResponseEntity.ok(radiographs);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RadiographDTO>> getRadiographsByStatus(@PathVariable String status) {
        log.debug("Fetching radiographs by status: {}", status);
        // This would need to be implemented in the service layer
        List<RadiographDTO> radiographs = radiographService.getAllRadiographs();
        return ResponseEntity.ok(radiographs);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRadiograph(@PathVariable Long id) {
        log.info("Deleting radiograph: {}", id);
        radiographService.deleteRadiograph(id);
        return ResponseEntity.noContent().build();
    }

    // History endpoints
    @GetMapping("/{id}/history")
    public ResponseEntity<List<RadiographHistoryDTO>> getRadiographHistory(@PathVariable Long id) {
        log.debug("Fetching history for radiograph: {}", id);
        List<RadiographHistoryDTO> history = historyService.getRadiographHistory(id);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST')")
    public ResponseEntity<RadiographHistoryDTO> addHistoryEntry(
            @PathVariable Long id,
            @Valid @RequestBody RadiographHistoryDTO historyDTO) {
        log.info("Adding history entry for radiograph: {}", id);
        historyDTO.setRadiographId(id);
        RadiographHistoryDTO created = historyService.addHistoryEntry(historyDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST')")
    public ResponseEntity<Void> updateRadiographStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        log.info("Updating status for radiograph: {} to {}", id, request.getNewStatus());
        historyService.updateRadiographStatus(id, request.getNewStatus(), 
                                         request.getPerformedBy(), request.getNotes(), request.getReason());
        return ResponseEntity.ok().build();
    }

    // DTO for status update request
    public static class StatusUpdateRequest {
        private RadiographStatus newStatus;
        private Long performedBy;
        private String notes;
        private String reason;

        // Getters and setters
        public RadiographStatus getNewStatus() { return newStatus; }
        public void setNewStatus(RadiographStatus newStatus) { this.newStatus = newStatus; }
        public Long getPerformedBy() { return performedBy; }
        public void setPerformedBy(Long performedBy) { this.performedBy = performedBy; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}