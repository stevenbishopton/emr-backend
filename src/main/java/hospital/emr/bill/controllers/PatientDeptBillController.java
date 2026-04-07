package hospital.emr.bill.controllers;

import hospital.emr.bill.dtos.PatientDeptBillDto;
import hospital.emr.bill.services.PatientDeptBillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emr/patient-dept-bills")
@RequiredArgsConstructor
public class PatientDeptBillController {

    private final PatientDeptBillService billService;

    @PostMapping
    public ResponseEntity<PatientDeptBillDto> createBill(@Valid @RequestBody PatientDeptBillDto dto) {
        PatientDeptBillDto response = billService.createBill(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PatientDeptBillDto>> getAllBills() {
        List<PatientDeptBillDto> responses = billService.getAllBills();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/admitted")
    public ResponseEntity<List<PatientDeptBillDto>> getAllAdmittedBills() {
        List<PatientDeptBillDto> responses = billService.getAllAdmittedBills();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<PatientDeptBillDto>> getAllBillsByVisitId(
            @PathVariable Long visitId){
        List<PatientDeptBillDto> responses = billService.getBillsByVisitId(visitId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/admitted/visit/{visitId}")
    public ResponseEntity<List<PatientDeptBillDto>> getAllAdmittedBillsByVisitId(
            @PathVariable Long visitId){
        List<PatientDeptBillDto> responses = billService.getAdmittedBillsByVisitId(visitId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/paid")
    public ResponseEntity<List<PatientDeptBillDto>> getPaidBills() {
        List<PatientDeptBillDto> responses = billService.getPaidBills();
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PatientDeptBillDto> getBillById(@PathVariable Long id) {
        PatientDeptBillDto response = billService.getBillById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<PatientDeptBillDto>> getBillsByPatientId(@PathVariable String patientId) {
        List<PatientDeptBillDto> responses = billService.getBillsByPatientId(patientId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/patient/name/{patientName}")
    public ResponseEntity<List<PatientDeptBillDto>> getBillsByPatientName(@PathVariable String patientName) {
        List<PatientDeptBillDto> responses = billService.getBillsByPatientName(patientName);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/issuer/{issuer}")
    public ResponseEntity<List<PatientDeptBillDto>> getBillsByIssuer(@PathVariable String issuer) {
        List<PatientDeptBillDto> responses = billService.getBillsByIssuer(issuer);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDeptBillDto> updateBill(
            @PathVariable Long id,
            @Valid @RequestBody PatientDeptBillDto dto) {
        PatientDeptBillDto response = billService.updateBill(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}