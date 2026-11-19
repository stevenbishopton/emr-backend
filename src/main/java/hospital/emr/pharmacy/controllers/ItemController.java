package hospital.emr.pharmacy.controllers;

import hospital.emr.pharmacy.dto.ItemDTO;
import hospital.emr.pharmacy.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emr/pharmacy/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // ✅ Create new item
    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemDTO itemDTO) {
        return new ResponseEntity<>(itemService.createItem(itemDTO), HttpStatus.CREATED);
    }

    // ✅ Get item by ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    // ✅ Get all items with pagination
    @GetMapping
    public ResponseEntity<Page<ItemDTO>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(itemService.getAllItems(pageable));
    }

    // ✅ Update item
    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(@PathVariable Long id, @RequestBody ItemDTO itemDTO) {
        return ResponseEntity.ok(itemService.updateItem(id, itemDTO));
    }

    // ✅ Delete item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ItemDTO>> searchItems(
            @RequestParam String name,
            Pageable pageable) {
        Page<ItemDTO> itemsPage = itemService.searchItemsByName(name, pageable);
        return ResponseEntity.ok(itemsPage);
    }

    // --- Inventory health: expiry & stock ---

    @GetMapping("/expired")
    public ResponseEntity<java.util.List<ItemDTO>> getExpiredItems() {
        return ResponseEntity.ok(itemService.getExpiredItems());
    }

    @GetMapping("/expiring")
    public ResponseEntity<java.util.List<ItemDTO>> getExpiringItems(
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(itemService.getExpiringItems(days));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<java.util.List<ItemDTO>> getLowStockItems(
            @RequestParam(defaultValue = "10") java.math.BigDecimal threshold) {
        return ResponseEntity.ok(itemService.getLowStockItems(threshold));
    }

    @GetMapping("/inventory-summary")
    public ResponseEntity<hospital.emr.pharmacy.dto.InventorySummaryDTO> getInventorySummary(
            @RequestParam(defaultValue = "10") java.math.BigDecimal threshold,
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(itemService.getInventorySummary(threshold, days));
    }

}
