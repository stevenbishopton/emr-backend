package hospital.emr.radiograph.services;

import hospital.emr.radiograph.dtos.RadiographCatalogDTO;
import hospital.emr.radiograph.enums.RadiographType;

import java.util.List;

public interface RadiographCatalogService {
    
    List<RadiographCatalogDTO> getAllCatalogItems();
    
    List<RadiographCatalogDTO> getActiveCatalogItems();
    
    RadiographCatalogDTO getCatalogItemById(Long id);
    
    List<RadiographCatalogDTO> getCatalogByType(RadiographType type);
    
    RadiographCatalogDTO createCatalogItem(RadiographCatalogDTO dto);
    
    RadiographCatalogDTO updateCatalogItem(Long id, RadiographCatalogDTO dto);
    
    void deleteCatalogItem(Long id);
    
    void activateCatalogItem(Long id);
    
    void deactivateCatalogItem(Long id);
    
    boolean existsByName(String name);
    
    boolean existsById(Long id);
}
