package hospital.emr.radiograph.controllers;

import hospital.emr.radiograph.dtos.RadiographVisitHistoryDTO;
import hospital.emr.radiograph.enums.RadiographStatus;
import hospital.emr.radiograph.services.RadiographVisitHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/emr/radiograph-visit-history")
@RequiredArgsConstructor
@Slf4j
public class RadiographVisitHistoryController {

    private final RadiographVisitHistoryService visitHistoryService;

    // CRUD operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_DOCTOR')")
    public ResponseEntity<RadiographVisitHistoryDTO> createVisitHistory(@Valid @RequestBody RadiographVisitHistoryDTO dto) {
        log.info("Creating radiograph visit history for patient: {}", dto.getPatient().getFullName());
        RadiographVisitHistoryDTO created = visitHistoryService.createVisitHistory(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST')")
    public ResponseEntity<RadiographVisitHistoryDTO> updateVisitHistory(
            @PathVariable Long id,
            @Valid @RequestBody RadiographVisitHistoryDTO dto) {
        log.info("Updating radiograph visit history with ID: {}", id);
        RadiographVisitHistoryDTO updated = visitHistoryService.updateVisitHistory(id, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR', 'ROLE_NURSE')")
    public ResponseEntity<RadiographVisitHistoryDTO> getVisitHistoryById(@PathVariable Long id) {
        log.debug("Fetching radiograph visit history with ID: {}", id);
        return visitHistoryService.getVisitHistoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteVisitHistory(@PathVariable Long id) {
        log.info("Deleting radiograph visit history with ID: {}", id);
        visitHistoryService.deleteVisitHistory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getAllVisitHistories() {
        log.debug("Fetching all radiograph visit histories");
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getAllVisitHistories();
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/paged")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR')")
    public ResponseEntity<Page<RadiographVisitHistoryDTO>> getAllVisitHistoriesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "visitDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        log.debug("Fetching all radiograph visit histories with pagination - page: {}, size: {}", page, size);
        Page<RadiographVisitHistoryDTO> resultPage = visitHistoryService.getAllVisitHistories(pageable);
        return ResponseEntity.ok(resultPage);
    }

    // Patient-specific operations
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR', 'ROLE_NURSE')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getPatientRadiographHistory(@PathVariable Long patientId) {
        log.debug("Fetching radiograph history for patient ID: {}", patientId);
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getPatientRadiographHistory(patientId);
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/patient/{patientId}/paged")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR', 'ROLE_NURSE')")
    public ResponseEntity<Page<RadiographVisitHistoryDTO>> getPatientRadiographHistoryPaged(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "visitDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        log.debug("Fetching radiograph history for patient ID: {} with pagination", patientId);
        Page<RadiographVisitHistoryDTO> resultPage = visitHistoryService.getPatientRadiographHistory(patientId, pageable);
        return ResponseEntity.ok(resultPage);
    }

    @GetMapping("/patient/{patientId}/date-range")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR', 'ROLE_NURSE')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getPatientRadiographHistoryByDateRange(
            @PathVariable Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.debug("Fetching radiograph history for patient ID: {} between {} and {}", patientId, startDate, endDate);
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getPatientRadiographHistoryByDateRange(patientId, startDate, endDate);
        return ResponseEntity.ok(histories);
    }

    // Visit-specific operations
    @GetMapping("/visit/{visitId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR', 'ROLE_NURSE')")
    public ResponseEntity<RadiographVisitHistoryDTO> getVisitHistoryByVisitId(@PathVariable Long visitId) {
        log.debug("Fetching radiograph visit history for visit ID: {}", visitId);
        return visitHistoryService.getVisitHistoryByVisitId(visitId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Status-based operations
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getVisitHistoriesByStatus(@PathVariable RadiographStatus status) {
        log.debug("Fetching radiograph visit histories with status: {}", status);
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getVisitHistoriesByStatus(status);
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/requiring-attention")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getVisitsRequiringAttention() {
        log.debug("Fetching radiograph visits requiring attention");
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getVisitsRequiringAttention();
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/completed-with-reports")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getCompletedVisitsWithReports() {
        log.debug("Fetching completed radiograph visits with reports");
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getCompletedVisitsWithReports();
        return ResponseEntity.ok(histories);
    }

    // Personnel-specific operations
    @GetMapping("/requested-by/{personnelId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getVisitHistoriesByRequestedBy(@PathVariable Long personnelId) {
        log.debug("Fetching radiograph visit histories requested by personnel ID: {}", personnelId);
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getVisitHistoriesByRequestedBy(personnelId);
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/performed-by/{personnelId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getVisitHistoriesByPerformedBy(@PathVariable Long personnelId) {
        log.debug("Fetching radiograph visit histories performed by personnel ID: {}", personnelId);
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getVisitHistoriesByPerformedBy(personnelId);
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/radiologist/{radiologistId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getVisitHistoriesByRadiologist(@PathVariable Long radiologistId) {
        log.debug("Fetching radiograph visit histories for radiologist ID: {}", radiologistId);
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getVisitHistoriesByRadiologist(radiologistId);
        return ResponseEntity.ok(histories);
    }

    // Date-based operations
    @GetMapping("/date-range")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getVisitHistoriesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.debug("Fetching radiograph visit histories between {} and {}", startDate, endDate);
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getVisitHistoriesByDateRange(startDate, endDate);
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/recent/{days}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getRecentVisits(@PathVariable int days) {
        log.debug("Fetching radiograph visits from last {} days", days);
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getRecentVisits(days);
        return ResponseEntity.ok(histories);
    }

    // Search operations
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> searchByPatient(@RequestParam String searchTerm) {
        log.debug("Searching radiograph visit histories by patient term: {}", searchTerm);
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.searchByPatient(searchTerm);
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/search/paged")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR')")
    public ResponseEntity<Page<RadiographVisitHistoryDTO>> searchByPatientPaged(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "visitDate"));
        log.debug("Searching radiograph visit histories by patient term: {} with pagination", searchTerm);
        Page<RadiographVisitHistoryDTO> resultPage = visitHistoryService.searchByPatient(searchTerm, pageable);
        return ResponseEntity.ok(resultPage);
    }

    // Visit type operations
    @GetMapping("/visit-type/{visitType}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST', 'ROLE_DOCTOR')")
    public ResponseEntity<List<RadiographVisitHistoryDTO>> getVisitHistoriesByVisitType(@PathVariable String visitType) {
        log.debug("Fetching radiograph visit histories by visit type: {}", visitType);
        List<RadiographVisitHistoryDTO> histories = visitHistoryService.getVisitHistoriesByVisitType(visitType);
        return ResponseEntity.ok(histories);
    }

    // Statistics operations
    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST')")
    public ResponseEntity<List<Object[]>> getVisitStatistics() {
        log.debug("Fetching radiograph visit statistics");
        List<Object[]> statistics = visitHistoryService.getVisitStatistics();
        return ResponseEntity.ok(statistics);
    }

    // Status update operations
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER', 'ROLE_RADIOLOGIST')")
    public ResponseEntity<RadiographVisitHistoryDTO> updateStatus(
            @PathVariable Long id,
            @RequestBody StatusUpdateRequest request) {
        
        log.info("Updating status for radiograph visit history ID: {} to {}", id, request.getNewStatus());
        RadiographVisitHistoryDTO updated = visitHistoryService.updateStatus(id, request.getNewStatus(), 
                                                                         request.getPerformedById(), request.getNotes());
        return ResponseEntity.ok(updated);
    }

    // Report operations
    @PutMapping("/{id}/radiologist-report")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOLOGIST')")
    public ResponseEntity<RadiographVisitHistoryDTO> addRadiologistReport(
            @PathVariable Long id,
            @RequestBody ReportRequest request) {
        
        log.info("Adding radiologist report for radiograph visit history ID: {}", id);
        RadiographVisitHistoryDTO updated = visitHistoryService.addRadiologistReport(id, request.getReport(), request.getRadiologistId());
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/technician-notes")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_RADIOGRAPHER')")
    public ResponseEntity<RadiographVisitHistoryDTO> addTechnicianNotes(
            @PathVariable Long id,
            @RequestBody NotesRequest request) {
        
        log.info("Adding technician notes for radiograph visit history ID: {}", id);
        RadiographVisitHistoryDTO updated = visitHistoryService.addTechnicianNotes(id, request.getNotes(), request.getTechnicianId());
        return ResponseEntity.ok(updated);
    }

    // DTOs for request bodies
    public static class StatusUpdateRequest {
        private RadiographStatus newStatus;
        private Long performedById;
        private String notes;

        // Getters and setters
        public RadiographStatus getNewStatus() { return newStatus; }
        public void setNewStatus(RadiographStatus newStatus) { this.newStatus = newStatus; }
        public Long getPerformedById() { return performedById; }
        public void setPerformedById(Long performedById) { this.performedById = performedById; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
    }

    public static class ReportRequest {
        private String report;
        private Long radiologistId;

        // Getters and setters
        public String getReport() { return report; }
        public void setReport(String report) { this.report = report; }
        public Long getRadiologistId() { return radiologistId; }
        public void setRadiologistId(Long radiologistId) { this.radiologistId = radiologistId; }
    }

    public static class NotesRequest {
        private String notes;
        private Long technicianId;

        // Getters and setters
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public Long getTechnicianId() { return technicianId; }
        public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }
    }
}
