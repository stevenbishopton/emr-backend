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

    // Add pharmacist-related methods as needed
}