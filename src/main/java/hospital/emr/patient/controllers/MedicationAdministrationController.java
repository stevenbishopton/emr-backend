package hospital.emr.patient.controllers;

import hospital.emr.patient.dtos.MedicationAdministrationDTO;
import hospital.emr.patient.services.MedicationAdministrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emr/medication-administrations")
@RequiredArgsConstructor
public class MedicationAdministrationController {

    private final MedicationAdministrationService service;

    @PostMapping
    public ResponseEntity<MedicationAdministrationDTO> record(
            @Valid @RequestBody MedicationAdministrationDTO dto) {
        return new ResponseEntity<>(service.record(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationAdministrationDTO> update(
            @PathVariable Long id, @RequestBody MedicationAdministrationDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/medical-history/{medicalHistoryId}")
    public ResponseEntity<List<MedicationAdministrationDTO>> byMedicalHistory(
            @PathVariable Long medicalHistoryId) {
        return ResponseEntity.ok(service.byMedicalHistory(medicalHistoryId));
    }

    @GetMapping("/entry/{entryId}")
    public ResponseEntity<List<MedicationAdministrationDTO>> byEntry(@PathVariable Long entryId) {
        return ResponseEntity.ok(service.byEntry(entryId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
