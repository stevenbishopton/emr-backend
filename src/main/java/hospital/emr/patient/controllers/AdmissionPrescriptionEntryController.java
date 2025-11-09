package hospital.emr.patient.controllers;

import hospital.emr.patient.dtos.AdmissionPrescriptionEntryDTO;
import hospital.emr.patient.services.AdmissionPrescriptionEntryService;
import hospital.emr.patient.services.PrescriptionChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/emr/prescription-entries")
@RequiredArgsConstructor
public class AdmissionPrescriptionEntryController {
    private final AdmissionPrescriptionEntryService service;
    private final PrescriptionChartService prescriptionChartService;


    //creates a prescription entry and chart (if not exist)
    @PostMapping
    public ResponseEntity<AdmissionPrescriptionEntryDTO> createPrescriptionEntry(@Valid @RequestBody AdmissionPrescriptionEntryDTO dto) {
        AdmissionPrescriptionEntryDTO createdDto = prescriptionChartService.addEntryToChart(dto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<AdmissionPrescriptionEntryDTO>> createPrescriptionEntries(@Valid @RequestBody List<AdmissionPrescriptionEntryDTO> dtos) {
        List<AdmissionPrescriptionEntryDTO> createdDtos = service.createPrescriptionEntries(dtos);
        return new ResponseEntity<>(createdDtos, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdmissionPrescriptionEntryDTO> getPrescriptionEntryById(@PathVariable Long id) {
        AdmissionPrescriptionEntryDTO dto = service.getPrescriptionEntryById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdmissionPrescriptionEntryDTO> updatePrescriptionEntry(@PathVariable Long id, @Valid @RequestBody AdmissionPrescriptionEntryDTO dto) {
        AdmissionPrescriptionEntryDTO updatedDto = service.updatePrescriptionEntry(id, dto);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescriptionEntry(@PathVariable Long id) {
        service.deletePrescriptionEntry(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}