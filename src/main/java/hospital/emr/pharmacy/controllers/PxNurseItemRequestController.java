package hospital.emr.pharmacy.controllers;

import hospital.emr.pharmacy.dto.PxNurseItemRequestDTO;
import hospital.emr.pharmacy.services.PxNurseItemRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emr/pharmacy/nurse-requests")
@RequiredArgsConstructor
@Slf4j
public class PxNurseItemRequestController {

    private final PxNurseItemRequestService pxNurseItemRequestService;

    @PostMapping
    public ResponseEntity<PxNurseItemRequestDTO> createPxNurseItemRequest(
            @RequestBody PxNurseItemRequestDTO pxNurseItemRequestDTO) {
        try {
            log.info("Received request to create nurse item request for patient: {}",
                    pxNurseItemRequestDTO.getPatientNames());
            PxNurseItemRequestDTO createdRequest =
                    pxNurseItemRequestService.createPxNurseItemRequest(pxNurseItemRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (Exception e) {
            log.error("Error creating nurse item request: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<PxNurseItemRequestDTO> updatePxNurseItemRequest(
            @RequestBody PxNurseItemRequestDTO pxNurseItemRequestDTO) {
        try {
            log.info("Received request to update nurse item request with ID: {}",
                    pxNurseItemRequestDTO.getId());
            Optional<PxNurseItemRequestDTO> updatedRequest =
                    pxNurseItemRequestService.updatePxNurseItemRequest(pxNurseItemRequestDTO);

            return updatedRequest
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error updating nurse item request: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PxNurseItemRequestDTO>> getAllPxNurseItemRequests() {
        try {
            log.info("Received request to fetch all nurse item requests");
            List<PxNurseItemRequestDTO> requests =
                    pxNurseItemRequestService.getAllPxNurseItemRequests();
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            log.error("Error fetching all nurse item requests: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PxNurseItemRequestDTO> getPxNurseItemRequestById(@PathVariable Long id) {
        try {
            log.info("Received request to fetch nurse item request by ID: {}", id);
            Optional<PxNurseItemRequestDTO> request =
                    pxNurseItemRequestService.getPxNurseItemRequestById(id);

            return request
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching nurse item request by ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PxNurseItemRequestDTO>> getPxNurseItemRequestsByPatientId(
            @PathVariable Long patientId) {
        try {
            log.info("Received request to fetch nurse item requests for patient ID: {}", patientId);
            List<PxNurseItemRequestDTO> requests =
                    pxNurseItemRequestService.getPxNurseItemRequestsByPatientId(patientId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            log.error("Error fetching nurse item requests for patient ID {}: {}", patientId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<PxNurseItemRequestDTO>> getPxNurseItemRequestsByVisitId(
            @PathVariable Long visitId) {
        try {
            log.info("Received request to fetch nurse item requests for visit ID: {}", visitId);
            List<PxNurseItemRequestDTO> requests =
                    pxNurseItemRequestService.getPxNurseItemRequestsByVisitId(visitId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            log.error("Error fetching nurse item requests for visit ID {}: {}", visitId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ward/{wardId}")
    public ResponseEntity<List<PxNurseItemRequestDTO>> getPxNurseItemRequestsByWardId(
            @PathVariable Long wardId) {
        try {
            log.info("Received request to fetch nurse item requests for ward ID: {}", wardId);
            List<PxNurseItemRequestDTO> requests =
                    pxNurseItemRequestService.getPxNurseItemRequestsByWardId(wardId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            log.error("Error fetching nurse item requests for ward ID {}: {}", wardId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/patient-code/{patientCode}")
    public ResponseEntity<List<PxNurseItemRequestDTO>> getPxNurseItemRequestsByPatientCode(
            @PathVariable String patientCode) {
        try {
            log.info("Received request to fetch nurse item requests for patient code: {}", patientCode);
            List<PxNurseItemRequestDTO> requests =
                    pxNurseItemRequestService.getPxNurseItemRequestsByPatientCode(patientCode);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            log.error("Error fetching nurse item requests for patient code {}: {}", patientCode, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePxNurseItemRequest(@PathVariable Long id) {
        try {
            log.info("Received request to delete nurse item request with ID: {}", id);
            boolean deleted = pxNurseItemRequestService.deletePxNurseItemRequest(id);

            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error deleting nurse item request with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}