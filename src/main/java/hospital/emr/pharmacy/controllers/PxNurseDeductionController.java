package hospital.emr.pharmacy.controllers;// PxNurseDeductionController.java


import hospital.emr.pharmacy.dto.PxNurseDeduction;
import hospital.emr.pharmacy.services.PxNurseDeductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emr/pharmacy/deductions")
@RequiredArgsConstructor
public class PxNurseDeductionController {

    private final PxNurseDeductionService deductionService;

    @PostMapping
    public ResponseEntity<PxNurseDeduction> createDeduction
            (@RequestBody PxNurseDeduction request) {
        try {
            PxNurseDeduction createdDeduction = deductionService.createDeduction(request);
            return ResponseEntity.ok(createdDeduction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PxNurseDeduction>> getAllDeductions() {
        List<PxNurseDeduction> deductions = deductionService.getAllDeductions();
        return ResponseEntity.ok(deductions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PxNurseDeduction> getDeductionById(@PathVariable Long id) {
        return deductionService.getDeductionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/request/{itemRequestId}")
    public ResponseEntity<List<PxNurseDeduction>> getDeductionsByRequestId(@PathVariable Long itemRequestId) {
        List<PxNurseDeduction> deductions = deductionService.getDeductionsByRequestId(itemRequestId);
        return ResponseEntity.ok(deductions);
    }

    @GetMapping("/dispenser/{dispenser}")
    public ResponseEntity<List<PxNurseDeduction>> getDeductionsByDispenser(@PathVariable String dispenser) {
        List<PxNurseDeduction> deductions = deductionService.getDeductionsByDispenser(dispenser);
        return ResponseEntity.ok(deductions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PxNurseDeduction> updateDeduction(
            @PathVariable Long id,
            @RequestBody PxNurseDeduction request) {
        try {
            PxNurseDeduction updatedDeduction = deductionService.updateDeduction(id, request);
            return ResponseEntity.ok(updatedDeduction);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeduction(@PathVariable Long id) {
        try {
            deductionService.deleteDeduction(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}