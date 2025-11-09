package hospital.emr.patient.controllers;

import hospital.emr.patient.dtos.PrescriptionAdministrationDTO;
import hospital.emr.patient.services.PrescriptionAdministrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/emr/prescription-administrations")
@RequiredArgsConstructor
public class PrescriptionAdministrationController {
    private final PrescriptionAdministrationService prescriptionAdministrationService;

    @PostMapping
    public ResponseEntity<PrescriptionAdministrationDTO> createAdministration(@Valid @RequestBody PrescriptionAdministrationDTO administrationDTO) {
        PrescriptionAdministrationDTO createdDto = prescriptionAdministrationService.createAdministration(administrationDTO);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PutMapping("/{administrationId}")
    public ResponseEntity<PrescriptionAdministrationDTO> updateAdministration(@PathVariable Long administrationId, @Valid @RequestBody PrescriptionAdministrationDTO administrationDTO) {
        PrescriptionAdministrationDTO updatedDto = prescriptionAdministrationService.updateAdministration(administrationId, administrationDTO);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/{administrationId}")
    public ResponseEntity<Void> deleteAdministration(@PathVariable Long administrationId) {
        prescriptionAdministrationService.deleteAdministration(administrationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/entry/{entryId}")
    public ResponseEntity<List<PrescriptionAdministrationDTO>> getAdministrationsByPrescriptionEntry(@PathVariable Long entryId) {
        List<PrescriptionAdministrationDTO> administrations = prescriptionAdministrationService.getAdministrationsByPrescriptionEntry(entryId);
        return new ResponseEntity<>(administrations, HttpStatus.OK);
    }

    @PostMapping("/custom")
    public ResponseEntity<PrescriptionAdministrationDTO> createCustomAdministration(
            @RequestParam Long entryId,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam String administeredBy) {

        PrescriptionAdministrationDTO dto = new PrescriptionAdministrationDTO();
        dto.setPrescriptionEntryId(entryId);
        dto.setAdministrationDate(LocalDate.parse(date));
        dto.setAdministrationTime(LocalTime.parse(time));
        dto.setAdministered(true);
        dto.setAdministeredBy(administeredBy);
        dto.setNotes("");

        PrescriptionAdministrationDTO createdDto = prescriptionAdministrationService.createAdministration(dto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }
}