package hospital.emr.radiograph.mappers;

import hospital.emr.radiograph.dtos.RadiographCatalogDTO;
import hospital.emr.radiograph.entities.RadiographCatalog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RadiographCatalogMapper {
    
    RadiographCatalogDTO toDto(RadiographCatalog entity);
    
    RadiographCatalog toEntity(RadiographCatalogDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    RadiographCatalog createEntity(RadiographCatalogDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(RadiographCatalogDTO dto, @MappingTarget RadiographCatalog entity);
}
