package hospital.emr.patient.controllers;

import hospital.emr.common.dtos.NoteDTO;
import hospital.emr.patient.dtos.AdmissionDTO;
import hospital.emr.patient.services.AdmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/emr/admissions")
@RequiredArgsConstructor
public class AdmissionController {
    private final AdmissionService admissionService;

    // Get all admissions (historical)
    @GetMapping
    public ResponseEntity<Page<AdmissionDTO>> getAllAdmissions(Pageable pageable) {
        Page<AdmissionDTO> admissions = admissionService.getAllAdmissionsHistorically(pageable);
        return ResponseEntity.ok(admissions);
    }

    // Get active admissions
    @GetMapping("/active")
    public ResponseEntity<List<AdmissionDTO>> getActiveAdmissions() {
        List<AdmissionDTO> activeAdmissions = admissionService.getActiveAdmissions();
        return ResponseEntity.ok(activeAdmissions);
    }

    // Get admission by ID
    @GetMapping("/{admissionId}")
    public ResponseEntity<AdmissionDTO> getAdmissionById(@PathVariable Long admissionId) {
        AdmissionDTO admission = admissionService.getAdmissionById(admissionId);
        return ResponseEntity.ok(admission);
    }

    // Get admissions by patient ID
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AdmissionDTO>> getAdmissionsByPatientId(@PathVariable Long patientId) {
        List<AdmissionDTO> admissions = admissionService.getAllAdmissionsByPatientId(patientId);
        return ResponseEntity.ok(admissions);
    }

    // Get admissions by ward ID
    @GetMapping("/ward/{wardId}")
    public ResponseEntity<List<AdmissionDTO>> getAdmissionsByWard(@PathVariable Long wardId) {
        List<AdmissionDTO> admissions = admissionService.getAdmissionsByWardHistorically(wardId);
        return ResponseEntity.ok(admissions);
    }

    // Get active admissions by ward ID
    @GetMapping("/ward/{wardId}/active")
    public ResponseEntity<List<AdmissionDTO>> getActiveAdmissionsByWard(@PathVariable Long wardId) {
        List<AdmissionDTO> admissions = admissionService.getActiveAdmissionsByWard(wardId);
        return ResponseEntity.ok(admissions);
    }

    // Create new admission
    @PostMapping
    public ResponseEntity<AdmissionDTO> createAdmission(@Valid @RequestBody AdmissionDTO admissionDTO) {
        AdmissionDTO createdAdmission = admissionService.admitPatient(admissionDTO);
        return new ResponseEntity<>(createdAdmission, HttpStatus.CREATED);
    }

    // Update admission
    @PutMapping
    public ResponseEntity<AdmissionDTO> updateAdmission(@Valid @RequestBody AdmissionDTO admissionDTO) {
        AdmissionDTO updatedAdmission = admissionService.updateAdmission(admissionDTO);
        return ResponseEntity.ok(updatedAdmission);
    }

    // ✅ NEW: Update only admission record
    @PutMapping("/{admissionId}/admission-record")
    public ResponseEntity<AdmissionDTO> updateAdmissionRecord(
            @PathVariable Long admissionId,
            @Valid @RequestBody NoteDTO admissionRecordDto) {

        AdmissionDTO updatedAdmission = admissionService.updateAdmissionRecord(admissionId, admissionRecordDto);
        return ResponseEntity.ok(updatedAdmission);
    }

    // Discharge patient
    @PutMapping("/{admissionId}/discharge")
    public ResponseEntity<AdmissionDTO> dischargePatient(
            @PathVariable Long admissionId,
            @RequestBody @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dischargeDate) {

        AdmissionDTO dischargedAdmission = admissionService.dischargePatient(admissionId, dischargeDate);
        return ResponseEntity.ok(dischargedAdmission);
    }

    // Check if patient is admitted
    @GetMapping("/patient/{patientId}/admitted")
    public ResponseEntity<Boolean> isPatientAdmitted(@PathVariable Long patientId) {
        boolean isAdmitted = admissionService.isPatientAdmitted(patientId);
        return ResponseEntity.ok(isAdmitted);
    }

    // Get admissions in date range
    @GetMapping("/date-range")
    public ResponseEntity<List<AdmissionDTO>> getAdmissionsInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<AdmissionDTO> admissions = admissionService.getAdmissionsInDateRange(startDate, endDate);
        return ResponseEntity.ok(admissions);
    }
}