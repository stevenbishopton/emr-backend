package hospital.emr.patient.controllers;

import hospital.emr.patient.dtos.PatientDTO;
import hospital.emr.patient.services.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/emr/patients")
@RequiredArgsConstructor
public class PatientController {
    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO createdPatient = patientService.createPatient(patientDTO);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
        return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<Page<PatientDTO>> getAllPatients(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        Page<PatientDTO> patients = patientService.getAllPatients(pageable);
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<PatientDTO> findPatientByPhoneNumber(@PathVariable String phoneNumber) {
        PatientDTO patient = patientService.findPatientByPhoneNumber(phoneNumber);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<PatientDTO> findPatientByCode(@PathVariable String code) {
        PatientDTO patient = patientService.findPatientByCode(code);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }

//    @GetMapping("/search/names/{names}")
//    public ResponseEntity<PatientDTO> findPatientByNames(@PathVariable String names) {
//        PatientDTO patient = patientService.findPatientByNames(names);
//        return new ResponseEntity<>(patient, HttpStatus.OK);
//    }
}