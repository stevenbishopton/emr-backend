package hospital.emr.pharmacy.controllers;

import hospital.emr.pharmacy.entities.PxPatientBill;
import hospital.emr.pharmacy.enums.PxPatientBillStatus;
import hospital.emr.pharmacy.services.PxPatientBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emr/pharmacy/bills")
@RequiredArgsConstructor
public class PxPatientBillController {

    private final PxPatientBillService billService;

    /**
     * Create a new bill — can be DRAFT or PAID.
     * If PAID, stock will be deducted automatically.
     */
    @PostMapping
    public ResponseEntity<PxPatientBill> createBill(@Valid @RequestBody PxPatientBill bill) {
        PxPatientBill created = billService.createBill(bill);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }



    /**
     * Get a bill by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PxPatientBill> getBill(@PathVariable Long id) {
        PxPatientBill bill = billService.getBill(id);
        return ResponseEntity.ok(bill);
    }

    /**
     * Get all bills (both draft and paid).
     */
    @GetMapping
    public ResponseEntity<List<PxPatientBill>> getAllBills() {
        List<PxPatientBill> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }


    /**
     * Get only paid bills.
     */
    @GetMapping("/paid")
    public ResponseEntity<List<PxPatientBill>> getPaidBills() {
        List<PxPatientBill> bills = billService.getPaidBills();
        return ResponseEntity.ok(bills);
    }

    /**
     * Delete a bill (you can later add a rule to only allow deleting drafts).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.noContent().build();
    }
}