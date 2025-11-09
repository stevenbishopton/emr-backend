package hospital.emr.pharmacy.services;

import hospital.emr.pharmacy.dto.PxPrescription;
import hospital.emr.pharmacy.entities.Item;
import hospital.emr.pharmacy.entities.PxPatientBill;
import hospital.emr.pharmacy.enums.PxPatientBillStatus;
import hospital.emr.pharmacy.repos.ItemRepository;
import hospital.emr.pharmacy.repos.PxPatientBillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PxPatientBillService {

    private final PxPatientBillRepository billRepository;
    private final ItemRepository itemRepository;

    /**
     * Creates a PAID bill and deducts inventory immediately
     */
    @Transactional
    public PxPatientBill createBill(PxPatientBill bill) {
        System.out.println("Creating bill: " + bill);

        // Ensure status is PAID for pharmacy bills
        bill.setStatus(PxPatientBillStatus.PAID);

        // Persist bill
        PxPatientBill saved = billRepository.save(bill);

        // Deduct inventory
        deductItemsFromInventory(bill.getPrescriptions());

        return saved;
    }

    /**
     * Deducts item quantities from stock based on prescription details
     */
    @Transactional
    private void deductItemsFromInventory(List<PxPrescription> prescriptions) {
        if (prescriptions == null || prescriptions.isEmpty()) return;

        for (PxPrescription px : prescriptions) {
            try {
                Long itemId = Long.parseLong(px.getItemId());
                BigDecimal qtyToDeduct = new BigDecimal(px.getQuantity());

                Item item = itemRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Item not found: " + px.getItemName()));

                if (item.getQuantity().compareTo(qtyToDeduct) < 0) {
                    throw new RuntimeException("Not enough stock for: " + item.getName());
                }

                item.setQuantity(item.getQuantity().subtract(qtyToDeduct));
                itemRepository.save(item);
                System.out.println("Deducted " + qtyToDeduct + " from item: " + item.getName());

            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid itemId or quantity format for prescription: " + px);
            }
        }
    }

    // Remove all draft-related methods:
    // - findDraftByPatient()
    // - createOrUpdateDraft()
    // - getDraftBills()
    // - updateBillStatus()

    @Transactional(readOnly = true)
    public PxPatientBill getBill(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<PxPatientBill> getAllBills() {
        return billRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PxPatientBill> getPaidBills() {
        return billRepository.findAllByStatus(PxPatientBillStatus.PAID);
    }

    @Transactional
    public void deleteBill(Long id) {
        if (!billRepository.existsById(id)) {
            throw new RuntimeException("Bill not found with ID: " + id);
        }
        billRepository.deleteById(id);
    }
}