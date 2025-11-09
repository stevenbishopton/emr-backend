package hospital.emr.patient.controllers;

import hospital.emr.patient.dtos.PrescriptionDTO;
import hospital.emr.patient.services.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid; // For DTO validation

import java.util.List;

@RestController
@RequestMapping("/emr/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionService prescriptionService;

    @PostMapping
    public ResponseEntity<PrescriptionDTO> createPrescription(@Valid @RequestBody PrescriptionDTO prescriptionDTO) {
        PrescriptionDTO createdPrescription = prescriptionService.createPrescription(prescriptionDTO);
        return new ResponseEntity<>(createdPrescription, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> findPrescriptionById(@PathVariable Long id) {
        PrescriptionDTO prescription = prescriptionService.findPrescriptionById(id);
        return new ResponseEntity<>(prescription, HttpStatus.OK);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PrescriptionDTO> findMostRecentPrescriptionByPatientId(@PathVariable Long patientId) {
        PrescriptionDTO prescription = prescriptionService.findMostRecentPrescriptionByPatientId(patientId);
        return ResponseEntity.ok(prescription);
    }


    @GetMapping("medical-history/{id}")
    public ResponseEntity<List<PrescriptionDTO>> findAllPrescriptionByMedicalHistoryId(@PathVariable Long id) {
        List<PrescriptionDTO> prescriptions = prescriptionService.findAllPrescriptionByMedicalHistoryId(id);
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PrescriptionDTO>> getAllPrescriptions() {
        List<PrescriptionDTO> prescriptions = prescriptionService.getAllPrescriptions();
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> updatePrescription(@PathVariable Long id, @Valid @RequestBody PrescriptionDTO prescriptionDTO) {
        PrescriptionDTO updatedPrescription = prescriptionService.updatePrescription(id, prescriptionDTO);
        return new ResponseEntity<>(updatedPrescription, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}