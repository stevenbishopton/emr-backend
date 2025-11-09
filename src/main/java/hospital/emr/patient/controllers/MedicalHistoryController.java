package hospital.emr.patient.controllers;

import hospital.emr.patient.dtos.MedicalHistoryDTO;
import hospital.emr.patient.dtos.VisitMedicalHistoryDTO;
import hospital.emr.patient.services.MedicalHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emr/medical-history")
@AllArgsConstructor
public class MedicalHistoryController {

    private final MedicalHistoryService medicalHistoryService;

    // Create medical history for a patient
    @PostMapping
    public ResponseEntity<MedicalHistoryDTO> createMedicalHistory(@RequestBody MedicalHistoryDTO medicalHistoryDTO) {
        try {
            MedicalHistoryDTO createdHistory = medicalHistoryService.createMedicalHistory(medicalHistoryDTO);
            return ResponseEntity.ok(createdHistory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get medical history for a patient (ungrouped - original format)
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<MedicalHistoryDTO> getMedicalHistory(@PathVariable Long patientId) {
        try {
            MedicalHistoryDTO medicalHistory = medicalHistoryService.getMedicalHistory(patientId);
            if (medicalHistory != null) {
                return ResponseEntity.ok(medicalHistory);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Get medical history grouped by visits (NEW ENDPOINT)
    @GetMapping("/patient/{patientId}/by-visits")
    public ResponseEntity<List<VisitMedicalHistoryDTO>> getMedicalHistoryByVisits(@PathVariable Long patientId) {
        try {
            List<VisitMedicalHistoryDTO> visitsHistory = medicalHistoryService.getMedicalHistoryByVisits(patientId);
            return ResponseEntity.ok(visitsHistory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Optional: Get specific visit medical history
    @GetMapping("/patient/{patientId}/visit/{visitId}")
    public ResponseEntity<VisitMedicalHistoryDTO> getMedicalHistoryForVisit(
            @PathVariable Long patientId,
            @PathVariable Long visitId) {
        try {
            List<VisitMedicalHistoryDTO> visitsHistory = medicalHistoryService.getMedicalHistoryByVisits(patientId);

            // Find the specific visit
            VisitMedicalHistoryDTO visitHistory = visitsHistory.stream()
                    .filter(visit -> visit.getVisitId().equals(visitId))
                    .findFirst()
                    .orElse(null);

            if (visitHistory != null) {
                return ResponseEntity.ok(visitHistory);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}