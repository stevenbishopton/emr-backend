package hospital.emr.radiograph.services;

import hospital.emr.radiograph.dtos.RadiographCatalogDTO;
import hospital.emr.radiograph.entities.RadiographCatalog;
import hospital.emr.radiograph.enums.RadiographType;
import hospital.emr.radiograph.mappers.RadiographCatalogMapper;
import hospital.emr.radiograph.repos.RadiographCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RadiographCatalogServiceImpl implements RadiographCatalogService {

    private final RadiographCatalogRepository catalogRepository;
    private final RadiographCatalogMapper catalogMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RadiographCatalogDTO> getAllCatalogItems() {
        log.debug("Fetching all radiograph catalog items");
        List<RadiographCatalog> items = catalogRepository.findAll();
        return items.stream()
                .map(catalogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographCatalogDTO> getActiveCatalogItems() {
        log.debug("Fetching active radiograph catalog items");
        List<RadiographCatalog> items = catalogRepository.findActiveCatalogItemsOrdered();
        return items.stream()
                .map(catalogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RadiographCatalogDTO getCatalogItemById(Long id) {
        log.debug("Fetching radiograph catalog item by id: {}", id);
        RadiographCatalog item = catalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Radiograph catalog item not found with id: " + id));
        return catalogMapper.toDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographCatalogDTO> getCatalogByType(RadiographType type) {
        log.debug("Fetching radiograph catalog items by type: {}", type);
        List<RadiographCatalog> items = catalogRepository.findByType(type);
        return items.stream()
                .map(catalogMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RadiographCatalogDTO createCatalogItem(RadiographCatalogDTO dto) {
        log.info("Creating new radiograph catalog item: {}", dto.getName());
        
        if (catalogRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Radiograph catalog item with name '" + dto.getName() + "' already exists");
        }

        RadiographCatalog entity = catalogMapper.toEntity(dto);
        entity.setActive(true);
        
        RadiographCatalog saved = catalogRepository.save(entity);
        log.info("Created radiograph catalog item with id: {}", saved.getId());
        
        return catalogMapper.toDto(saved);
    }

    @Override
    public RadiographCatalogDTO updateCatalogItem(Long id, RadiographCatalogDTO dto) {
        log.info("Updating radiograph catalog item with id: {}", id);
        
        RadiographCatalog existing = catalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Radiograph catalog item not found with id: " + id));

        // Check if name is being changed and if new name already exists
        if (!existing.getName().equals(dto.getName()) && catalogRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Radiograph catalog item with name '" + dto.getName() + "' already exists");
        }

        catalogMapper.updateEntityFromDto(dto, existing);
        RadiographCatalog updated = catalogRepository.save(existing);
        
        log.info("Updated radiograph catalog item with id: {}", updated.getId());
        return catalogMapper.toDto(updated);
    }

    @Override
    public void deleteCatalogItem(Long id) {
        log.info("Deleting radiograph catalog item with id: {}", id);
        
        if (!catalogRepository.existsById(id)) {
            throw new RuntimeException("Radiograph catalog item not found with id: " + id);
        }

        catalogRepository.deleteById(id);
        log.info("Deleted radiograph catalog item with id: {}", id);
    }

    @Override
    public void activateCatalogItem(Long id) {
        log.info("Activating radiograph catalog item with id: {}", id);
        
        RadiographCatalog item = catalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Radiograph catalog item not found with id: " + id));
        
        item.setActive(true);
        catalogRepository.save(item);
        log.info("Activated radiograph catalog item with id: {}", id);
    }

    @Override
    public void deactivateCatalogItem(Long id) {
        log.info("Deactivating radiograph catalog item with id: {}", id);
        
        RadiographCatalog item = catalogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Radiograph catalog item not found with id: " + id));
        
        item.setActive(false);
        catalogRepository.save(item);
        log.info("Deactivated radiograph catalog item with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return catalogRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return catalogRepository.existsById(id);
    }
}
