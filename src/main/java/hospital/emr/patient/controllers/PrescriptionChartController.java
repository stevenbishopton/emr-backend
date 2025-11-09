package hospital.emr.patient.controllers;

import hospital.emr.patient.dtos.AdmissionPrescriptionEntryDTO;
import hospital.emr.patient.dtos.PrescriptionCellUpdateDTO;
import hospital.emr.patient.dtos.PrescriptionChartDTO;
import hospital.emr.patient.services.PrescriptionChartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emr/prescription-charts")
@RequiredArgsConstructor
public class PrescriptionChartController {
    private final PrescriptionChartService prescriptionChartService;

    @PostMapping("/entries")
    public ResponseEntity<AdmissionPrescriptionEntryDTO> addEntryToChart(@Valid @RequestBody AdmissionPrescriptionEntryDTO entryDTO) {
        AdmissionPrescriptionEntryDTO createdEntry = prescriptionChartService.addEntryToChart(entryDTO);
        return new ResponseEntity<>(createdEntry, HttpStatus.CREATED);
    }

    @GetMapping("/admission/{admissionId}")
    public ResponseEntity<PrescriptionChartDTO> getChartByAdmissionId(@PathVariable Long admissionId) {
        PrescriptionChartDTO chart = prescriptionChartService.getPrescriptionChartByAdmissionId(admissionId);
        return new ResponseEntity<>(chart, HttpStatus.OK);
    }

    @PutMapping("/cell")
    public ResponseEntity<Void> updatePrescriptionCell(@RequestBody PrescriptionCellUpdateDTO updateRequest) {
        prescriptionChartService.updatePrescriptionCell(updateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<Void> deletePrescriptionEntry(@PathVariable Long entryId) {
        prescriptionChartService.deletePrescriptionEntry(entryId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{chartId}")
    public ResponseEntity<Void> deleteChart(@PathVariable Long chartId) {
        prescriptionChartService.deletePrescriptionChart(chartId);
        return ResponseEntity.noContent().build();
    }
}