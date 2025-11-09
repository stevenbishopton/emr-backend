package hospital.emr.bill.controllers;

import hospital.emr.bill.dtos.BillDTO;
import hospital.emr.bill.services.BillService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/emr/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping
    public ResponseEntity<BillDTO> createBill(@Valid @RequestBody BillDTO billDTO) {
        log.info("Received request to create Bill for patientId={}", billDTO.getPatientId());
        BillDTO createdBill = billService.createBill(billDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDTO> getBillById(@PathVariable Long id) {
        log.info("Received request to fetch Bill with id={}", id);
        BillDTO bill = billService.getBill(id);
        return ResponseEntity.ok(bill);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<BillDTO>> getBillsByPatientId(@PathVariable Long patientId) {
        List<BillDTO> bills = billService.getBillByPatientId(patientId);
        return ResponseEntity.ok(bills);
    }
//    @GetMapping
//    public ResponseEntity<List<BillDTO>> getAllBills() {
//        log.info("Received request to fetch all Bills");
//        List<BillDTO> bills = billService.listBills();
//        return ResponseEntity.ok(bills);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<BillDTO> updateBill(
            @PathVariable Long id,
            @Valid @RequestBody BillDTO billDTO) {
        log.info("Received request to update Bill with id={}", id);
        BillDTO updatedBill = billService.updateBill(id, billDTO);
        return ResponseEntity.ok(updatedBill);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        log.info("Received request to delete Bill with id={}", id);
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}
