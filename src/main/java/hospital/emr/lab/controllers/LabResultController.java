package hospital.emr.lab.controllers;

import hospital.emr.lab.dtos.CreateLabTestResultRequest;
import hospital.emr.lab.dtos.LabTestResultDTO;
import hospital.emr.lab.dtos.UpdateLabTestResultRequest;
import hospital.emr.lab.enums.TestStatus;
import hospital.emr.lab.services.LabResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Lab Test Results
 * Provides endpoints for creating, updating, and retrieving lab test results
 */
@RestController
@RequestMapping("/emr/lab/results")
@RequiredArgsConstructor
public class LabResultController {

    private final LabResultService resultService;

    /**
     * Create a new lab test result
     */
    @PostMapping
    public ResponseEntity<LabTestResultDTO> createResult(@Valid @RequestBody CreateLabTestResultRequest request) {
        LabTestResultDTO created = resultService.createResult(request);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Get result by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LabTestResultDTO> getResultById(@PathVariable Long id) {
        return ResponseEntity.ok(resultService.getResultById(id));
    }

    /**
     * Update a lab test result
     */
    @PutMapping("/{id}")
    public ResponseEntity<LabTestResultDTO> updateResult(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLabTestResultRequest request) {
        return ResponseEntity.ok(resultService.updateResult(id, request));
    }

    /**
     * Get all results for a patient
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabTestResultDTO>> getResultsByPatientId(@PathVariable Long patientId) {
        return ResponseEntity.ok(resultService.getResultsByPatientId(patientId));
    }

    /**
     * Get all results for a visit
     */
    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<LabTestResultDTO>> getResultsByVisitId(@PathVariable Long visitId) {
        return ResponseEntity.ok(resultService.getResultsByVisitId(visitId));
    }

    /**
     * Get results by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LabTestResultDTO>> getResultsByStatus(@PathVariable TestStatus status) {
        return ResponseEntity.ok(resultService.getResultsByStatus(status));
    }

    /**
     * Get pending results (for lab technicians)
     */
    @GetMapping("/pending")
    public ResponseEntity<List<LabTestResultDTO>> getPendingResults() {
        return ResponseEntity.ok(resultService.getPendingResults());
    }
}
