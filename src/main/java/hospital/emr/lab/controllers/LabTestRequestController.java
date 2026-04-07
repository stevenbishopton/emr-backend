package hospital.emr.lab.controllers;

import hospital.emr.lab.dtos.CreateLabTestRequestDTO;
import hospital.emr.lab.dtos.LabTestRequestResponseDTO;
import hospital.emr.lab.dtos.UpdateLabTestRequestDTO;
import hospital.emr.lab.services.LabTestRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emr/lab-test-requests")
public class LabTestRequestController {

    @Autowired
    private LabTestRequestService labTestRequestService;

    @PostMapping
    public ResponseEntity<LabTestRequestResponseDTO> createLabTestRequest(
            @RequestBody CreateLabTestRequestDTO requestDTO) {
        LabTestRequestResponseDTO createdRequest = labTestRequestService.createLabTestRequest(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabTestRequestResponseDTO> getLabTestRequestById(@PathVariable Long id) {
        LabTestRequestResponseDTO labTestRequest = labTestRequestService.getLabTestRequestById(id);
        return ResponseEntity.ok(labTestRequest);
    }

    @GetMapping
    public ResponseEntity<List<LabTestRequestResponseDTO>> getAllLabTestRequests() {
        List<LabTestRequestResponseDTO> labTestRequests = labTestRequestService.getAllLabTestRequests();
        return ResponseEntity.ok(labTestRequests);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabTestRequestResponseDTO> updateLabTestRequest(
            @PathVariable Long id,
            @RequestBody UpdateLabTestRequestDTO requestDTO) {
        LabTestRequestResponseDTO updatedRequest = labTestRequestService.updateLabTestRequest(id, requestDTO);
        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabTestRequest(@PathVariable Long id) {
        labTestRequestService.deleteLabTestRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<LabTestRequestResponseDTO>> getLabTestRequestsByPatientId(
            @PathVariable Long patientId) {
        List<LabTestRequestResponseDTO> labTestRequests =
                labTestRequestService.getLabTestRequestsByPatientId(patientId);
        return ResponseEntity.ok(labTestRequests);
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<LabTestRequestResponseDTO>> getLabTestRequestsByVisitId(
            @PathVariable Long visitId) {
        List<LabTestRequestResponseDTO> labTestRequests =
                labTestRequestService.getLabTestRequestsByVisitId(visitId);
        return ResponseEntity.ok(labTestRequests);
    }

    @GetMapping("/medical-history/{medicalHistoryId}")
    public ResponseEntity<List<LabTestRequestResponseDTO>> getLabTestRequestsByMedicalHistoryId(
            @PathVariable Long medicalHistoryId) {
        List<LabTestRequestResponseDTO> labTestRequests =
                labTestRequestService.getLabTestRequestsByMedicalHistoryId(medicalHistoryId);
        return ResponseEntity.ok(labTestRequests);
    }

    @GetMapping("/requested-by/{requestedBy}")
    public ResponseEntity<List<LabTestRequestResponseDTO>> getLabTestRequestsByRequestedBy(
            @PathVariable String requestedBy) {
        List<LabTestRequestResponseDTO> labTestRequests =
                labTestRequestService.getLabTestRequestsByRequestedBy(requestedBy);
        return ResponseEntity.ok(labTestRequests);
    }

    @GetMapping("/lab-test/{testId}")
    public ResponseEntity<List<LabTestRequestResponseDTO>> getLabTestRequestsByLabTestId(
            @PathVariable Long testId) {
        List<LabTestRequestResponseDTO> labTestRequests =
                labTestRequestService.getLabTestRequestsByLabTestId(testId);
        return ResponseEntity.ok(labTestRequests);
    }

    @GetMapping("/patient/{patientId}/visit/{visitId}")
    public ResponseEntity<List<LabTestRequestResponseDTO>> getLabTestRequestsByPatientAndVisit(
            @PathVariable Long patientId, @PathVariable Long visitId) {
        List<LabTestRequestResponseDTO> labTestRequests =
                labTestRequestService.getLabTestRequestsByPatientAndVisit(patientId, visitId);
        return ResponseEntity.ok(labTestRequests);
    }
}