package hospital.emr.pharmacy.services;

import hospital.emr.pharmacy.dto.PxNurseDeduction;
import hospital.emr.pharmacy.dto.PxItemDeducted;
import hospital.emr.pharmacy.entities.Item;
import hospital.emr.pharmacy.repos.PxNurseDeductionRepository;
import hospital.emr.pharmacy.services.ItemService;
import hospital.emr.pharmacy.repos.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PxNurseDeductionService {

    private final PxNurseDeductionRepository deductionRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public PxNurseDeduction createDeduction(PxNurseDeduction deduction) {
        try {
            // Validate deduction
            validateDeduction(deduction);

            // Deduct items from inventory
            deductItemsFromInventory(deduction.getItemDeductedList());

            // Save deduction record
            PxNurseDeduction savedDeduction = deductionRepository.save(deduction);

            log.info("Created deduction record for request ID: {}", deduction.getItemRequestId());
            return savedDeduction;

        } catch (Exception e) {
            log.error("Error creating deduction record: {}", e.getMessage());
            throw new RuntimeException("Failed to create deduction record: " + e.getMessage());
        }
    }

    public List<PxNurseDeduction> getAllDeductions() {
        return deductionRepository.findAll();
    }

    public Optional<PxNurseDeduction> getDeductionById(Long id) {
        return deductionRepository.findById(id);
    }

    public List<PxNurseDeduction> getDeductionsByRequestId(Long itemRequestId) {
        return deductionRepository.findByItemRequestId(itemRequestId);
    }

    public List<PxNurseDeduction> getDeductionsByDispenser(String dispenser) {
        return deductionRepository.findByDispenserContainingIgnoreCase(dispenser);
    }

    @Transactional
    public PxNurseDeduction updateDeduction(Long id, PxNurseDeduction deduction) {
        Optional<PxNurseDeduction> existingDeduction = deductionRepository.findById(id);

        if (existingDeduction.isPresent()) {
            // Return previous items to inventory
            returnItemsToInventory(existingDeduction.get().getItemDeductedList());

            // Deduct new items
            deductItemsFromInventory(deduction.getItemDeductedList());

            // Update deduction
            PxNurseDeduction existing = existingDeduction.get();
            existing.setItemRequestId(deduction.getItemRequestId());
            existing.setDispenser(deduction.getDispenser());
            existing.setItemDeductedList(deduction.getItemDeductedList());

            PxNurseDeduction updatedDeduction = deductionRepository.save(existing);
            log.info("Updated deduction record with ID: {}", id);
            return updatedDeduction;
        }

        throw new RuntimeException("Deduction record not found with ID: " + id);
    }

    @Transactional
    public void deleteDeduction(Long id) {
        Optional<PxNurseDeduction> deduction = deductionRepository.findById(id);

        if (deduction.isPresent()) {
            // Return items to inventory
            returnItemsToInventory(deduction.get().getItemDeductedList());

            deductionRepository.deleteById(id);
            log.info("Deleted deduction record with ID: {}", id);
        } else {
            throw new RuntimeException("Deduction record not found with ID: " + id);
        }
    }

    private void validateDeduction(PxNurseDeduction deduction) {
        if (deduction.getItemRequestId() == null) {
            throw new IllegalArgumentException("Item request ID is required");
        }
        if (deduction.getDispenser() == null || deduction.getDispenser().trim().isEmpty()) {
            throw new IllegalArgumentException("Dispenser name is required");
        }
        if (deduction.getItemDeductedList() == null || deduction.getItemDeductedList().isEmpty()) {
            throw new IllegalArgumentException("At least one item must be deducted");
        }

        for (PxItemDeducted item : deduction.getItemDeductedList()) {
            if (item.getItemId() == null) {
                throw new IllegalArgumentException("Item ID is required for all items");
            }
            if (item.getQuantityDeducted() == null || item.getQuantityDeducted().trim().isEmpty()) {
                throw new IllegalArgumentException("Quantity deducted is required for all items");
            }

            // Pre-validate that the item exists and has sufficient quantity
            validateItemQuantity(item.getItemId(), item.getQuantityDeducted());
        }
    }

    private void validateItemQuantity(Long itemId, String quantityDeducted) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Inventory item not found with ID: " + itemId));

        BigDecimal quantityToDeduct = parseQuantityToBigDecimal(quantityDeducted);

        if (item.getQuantity().compareTo(quantityToDeduct) < 0) {
            throw new RuntimeException("Insufficient quantity for item: " + item.getName() +
                    ". Available: " + item.getQuantity() + ", Required: " + quantityToDeduct);
        }
    }

    private void deductItemsFromInventory(List<PxItemDeducted> itemsToDeduct) {
        for (PxItemDeducted item : itemsToDeduct) {
            Item inventoryItem = itemRepository.findById(item.getItemId())
                    .orElseThrow(() -> new RuntimeException("Inventory item not found with ID: " + item.getItemId()));

            BigDecimal quantityToDeduct = parseQuantityToBigDecimal(item.getQuantityDeducted());

            // Double-check quantity (though validation should have caught this)
            if (inventoryItem.getQuantity().compareTo(quantityToDeduct) >= 0) {
                // Subtract the deducted quantity using BigDecimal arithmetic
                BigDecimal newQuantity = inventoryItem.getQuantity().subtract(quantityToDeduct);
                inventoryItem.setQuantity(newQuantity);

                itemRepository.save(inventoryItem);
                log.info("Deducted {} from inventory item: {}. New quantity: {}",
                        item.getQuantityDeducted(), inventoryItem.getName(), newQuantity);
            } else {
                throw new RuntimeException("Insufficient quantity for item: " + inventoryItem.getName() +
                        ". Available: " + inventoryItem.getQuantity() + ", Required: " + quantityToDeduct);
            }
        }
    }

    private void returnItemsToInventory(List<PxItemDeducted> itemsToReturn) {
        for (PxItemDeducted item : itemsToReturn) {
            Optional<Item> inventoryItemOpt = itemRepository.findById(item.getItemId());

            if (inventoryItemOpt.isPresent()) {
                Item inventoryItem = inventoryItemOpt.get();
                BigDecimal quantityToReturn = parseQuantityToBigDecimal(item.getQuantityDeducted());

                // Add the returned quantity back using BigDecimal arithmetic
                BigDecimal newQuantity = inventoryItem.getQuantity().add(quantityToReturn);
                inventoryItem.setQuantity(newQuantity);

                itemRepository.save(inventoryItem);
                log.info("Returned {} to inventory item: {}. New quantity: {}",
                        item.getQuantityDeducted(), inventoryItem.getName(), newQuantity);
            } else {
                log.warn("Inventory item not found when returning items: ID {}", item.getItemId());
            }
        }
    }

    private BigDecimal parseQuantityToBigDecimal(String quantityStr) {
        try {
            // Remove any non-numeric characters except decimal point and negative sign
            String numericPart = quantityStr.replaceAll("[^0-9.-]", "");
            if (numericPart.isEmpty()) {
                throw new NumberFormatException("No numeric quantity found in: " + quantityStr);
            }
            BigDecimal result = new BigDecimal(numericPart);

            // Ensure quantity is positive
            if (result.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Quantity must be positive: " + quantityStr);
            }

            return result;
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid quantity format: " + quantityStr + ". Please use numeric values.");
        }
    }

    public Item getItemDetails(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + itemId));
    }
}