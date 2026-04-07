package hospital.emr.admin.controllers;

import hospital.emr.admin.entities.PurchaseReceipt;

import hospital.emr.admin.service.PurchaseReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emr/purchase-receipts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PurchaseReceiptController {

    private final PurchaseReceiptService purchaseReceiptService;

    @PostMapping
    public ResponseEntity<PurchaseReceipt> createPurchaseReceipt(@RequestBody PurchaseReceipt purchaseReceipt) {
        try {
            PurchaseReceipt created = purchaseReceiptService.createPurchaseReceipt(purchaseReceipt);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseReceipt> getPurchaseReceiptById(@PathVariable Long id) {
        try {
            PurchaseReceipt receipt = purchaseReceiptService.getPurchaseReceiptById(id);
            return new ResponseEntity<>(receipt, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<List<PurchaseReceipt>> getAllPurchaseReceipts() {
        try {
            List<PurchaseReceipt> receipts = purchaseReceiptService.getAllPurchaseReceipts();
            return new ResponseEntity<>(receipts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseReceipt> updatePurchaseReceipt(
            @PathVariable Long id,
            @RequestBody PurchaseReceipt purchaseReceipt) {
        try {
            PurchaseReceipt updated = purchaseReceiptService.updatePurchaseReceipt(id, purchaseReceipt);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseReceipt(@PathVariable Long id) {
        try {
            purchaseReceiptService.deletePurchaseReceipt(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search/client")
    public ResponseEntity<List<PurchaseReceipt>> searchByClientName(@RequestParam String clientName) {
        try {
            List<PurchaseReceipt> receipts = purchaseReceiptService.searchByClientName(clientName);
            return new ResponseEntity<>(receipts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search/ordered-by")
    public ResponseEntity<List<PurchaseReceipt>> searchByOrderedBy(@RequestParam String orderedBy) {
        try {
            List<PurchaseReceipt> receipts = purchaseReceiptService.searchByOrderedBy(orderedBy);
            return new ResponseEntity<>(receipts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}