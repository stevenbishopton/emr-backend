package hospital.emr.pharmacy.services;

import hospital.emr.pharmacy.dto.*;
import hospital.emr.pharmacy.entities.Item;
import hospital.emr.pharmacy.mappers.ItemMapper;
import hospital.emr.pharmacy.repos.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper ItemMapper;


    @Transactional
    public ItemDTO createItem(ItemDTO dto) {
        Item item = ItemMapper.toEntity(dto);
        itemRepository.save(item);
        return ItemMapper.toDto(item);
    }

    @Transactional(readOnly = true)
    public Page<ItemDTO> searchItemsByName(String name, Pageable pageable) {
        return itemRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(ItemMapper::toDto);
    }

    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return ItemMapper.toDto(item);
    }

    public Page<ItemDTO> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable).map(ItemMapper::toDto);
    }


    @Transactional
    public ItemDTO updateItem(Long id, ItemDTO dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        ItemMapper.updateItemFromDto(dto, item);
        itemRepository.save(item);
        return ItemMapper.toDto(item);
    }

    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found");
        }
        itemRepository.deleteById(id);
    }

    // --- Inventory expiry & stock health ---

    @Transactional(readOnly = true)
    public List<ItemDTO> getExpiredItems() {
        return itemRepository.findExpiredItems().stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getExpiringItems(int days) {
        LocalDate today = LocalDate.now();
        LocalDate future = today.plusDays(Math.max(days, 0));
        return itemRepository.findItemsExpiringSoon(future).stream()
                .filter(i -> i.getExpirationDate() != null
                        && !i.getExpirationDate().isBefore(today))
                .map(ItemMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> getLowStockItems(BigDecimal threshold) {
        BigDecimal limit = threshold != null ? threshold : BigDecimal.TEN;
        return itemRepository.findItemsWithLowStock(limit).stream()
                .map(ItemMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public InventorySummaryDTO getInventorySummary(BigDecimal threshold, int expiringDays) {
        BigDecimal limit = threshold != null ? threshold : BigDecimal.TEN;
        int window = expiringDays > 0 ? expiringDays : 30;

        InventorySummaryDTO summary = new InventorySummaryDTO();
        summary.setTotalItems(itemRepository.count());
        summary.setExpiredItems(itemRepository.countExpiredItems());
        summary.setExpiringSoonItems(getExpiringItems(window).size());
        summary.setLowStockItems(itemRepository.countItemsWithLowStock(limit));
        summary.setOutOfStockItems(itemRepository.findItemsWithZeroStock().size());
        summary.setTotalStockValue(itemRepository.totalStockValue());
        summary.setExpiringWindowDays(window);
        summary.setLowStockThreshold(limit);
        return summary;
    }
}