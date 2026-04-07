package hospital.emr.admin.service;

import hospital.emr.admin.entities.PurchaseReceipt;
import hospital.emr.admin.repos.PurchaseReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseReceiptService {

    private final PurchaseReceiptRepository purchaseReceiptRepository;

    public PurchaseReceipt createPurchaseReceipt(PurchaseReceipt purchaseReceipt) {
        return purchaseReceiptRepository.save(purchaseReceipt);
    }

    @Transactional(readOnly = true)
    public PurchaseReceipt getPurchaseReceiptById(Long id) {
        return purchaseReceiptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase receipt not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<PurchaseReceipt> getAllPurchaseReceipts() {
        return purchaseReceiptRepository.findAll();
    }

    public PurchaseReceipt updatePurchaseReceipt(Long id, PurchaseReceipt purchaseReceipt) {
        PurchaseReceipt existing = getPurchaseReceiptById(id);

        // Update fields - you could use BeanUtils or MapStruct if you prefer
        existing.setClientName(purchaseReceipt.getClientName());
        existing.setOrderedBy(purchaseReceipt.getOrderedBy());
        existing.setPurchasedItems(purchaseReceipt.getPurchasedItems());
        existing.setTotalAmount(purchaseReceipt.getTotalAmount());
        existing.setNotes(purchaseReceipt.getNotes());
        existing.setDateOfArrival(purchaseReceipt.getDateOfArrival());

        return purchaseReceiptRepository.save(existing);
    }

    public void deletePurchaseReceipt(Long id) {
        if (!purchaseReceiptRepository.existsById(id)) {
            throw new RuntimeException("Purchase receipt not found with ID: " + id);
        }
        purchaseReceiptRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PurchaseReceipt> searchByClientName(String clientName) {
        return purchaseReceiptRepository.findByClientNameContainingIgnoreCase(clientName);
    }

    @Transactional(readOnly = true)
    public List<PurchaseReceipt> searchByOrderedBy(String orderedBy) {
        return purchaseReceiptRepository.findByOrderedByContainingIgnoreCase(orderedBy);
    }
}