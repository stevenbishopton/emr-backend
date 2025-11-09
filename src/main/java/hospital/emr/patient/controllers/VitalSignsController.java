package hospital.emr.patient.controllers;

import hospital.emr.patient.dtos.VitalSignsDTO;
import hospital.emr.patient.services.VitalSignsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emr/vitals")
@RequiredArgsConstructor
public class VitalSignsController {

    private final VitalSignsService vitalSignsService;

    @PostMapping()
    public ResponseEntity<VitalSignsDTO> recordVitalSigns(@RequestBody VitalSignsDTO dto) {
        return ResponseEntity.ok(vitalSignsService.recordVitalSigns(dto));
    }

    @GetMapping("/history/{medicalHistoryId}")
    public ResponseEntity<List<VitalSignsDTO>> getVitalsByMedicalHistory(@PathVariable Long medicalHistoryId) {
        return ResponseEntity.ok(vitalSignsService.getVitalsByMedicalHistory(medicalHistoryId));
    }

    @GetMapping("/history/{medicalHistoryId}/latest")
    public ResponseEntity<VitalSignsDTO> getLatestVitalsByMedicalHistory(@PathVariable Long medicalHistoryId) {
        Optional<VitalSignsDTO> latestVitals = vitalSignsService.getLatestVitalsByMedicalHistory(medicalHistoryId);
        return latestVitals.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/visit/{visitId}/latest")
    public ResponseEntity<VitalSignsDTO> getLatestVitalsByVisit(@PathVariable Long visitId) {
        Optional<VitalSignsDTO> latestVitals = vitalSignsService.getLatestVitalsByVisit(visitId);
        return latestVitals.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/patient/{patientId}/latest")
    public ResponseEntity<VitalSignsDTO> getLatestVitalsByPatient(@PathVariable Long patientId) {
        Optional<VitalSignsDTO> latestVitals = vitalSignsService.getLatestVitalsByPatient(patientId);
        return latestVitals.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<VitalSignsDTO>> getVitalsByVisit(@PathVariable Long visitId) {
        return ResponseEntity.ok(vitalSignsService.getVitalsByVisit(visitId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VitalSignsDTO> getVitalSigns(@PathVariable Long id) {
        return ResponseEntity.ok(vitalSignsService.getVitalSignsById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVitalSigns(@PathVariable Long id) {
        vitalSignsService.deleteVitalSigns(id);
        return ResponseEntity.noContent().build();
    }
}