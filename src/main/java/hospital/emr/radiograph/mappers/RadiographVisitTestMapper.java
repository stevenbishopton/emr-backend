package hospital.emr.radiograph.mappers;

import hospital.emr.radiograph.dtos.RadiographVisitTestDTO;
import hospital.emr.radiograph.entities.RadiographVisitTest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RadiographVisitTestMapper {

    @Mapping(target = "catalogItem", ignore = true)
    @Mapping(target = "visitHistoryId", ignore = true)
    RadiographVisitTestDTO toDto(RadiographVisitTest entity);

    @Mapping(target = "catalogItem", ignore = true)
    @Mapping(target = "visitHistory", ignore = true)
    RadiographVisitTest toEntity(RadiographVisitTestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "catalogItem", ignore = true)
    @Mapping(target = "visitHistory", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    RadiographVisitTest createEntity(RadiographVisitTestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "catalogItem", ignore = true)
    @Mapping(target = "visitHistory", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(RadiographVisitTestDTO dto, @MappingTarget RadiographVisitTest entity);

    List<RadiographVisitTestDTO> toDtoList(List<RadiographVisitTest> entities);

    // Helper mapping method
    @Named("mapToCatalogItemDTO")
    default RadiographVisitTestDTO.RadiographCatalogDTO mapToCatalogItemDTO(hospital.emr.radiograph.entities.RadiographCatalog catalogItem) {
        if (catalogItem == null) return null;
        RadiographVisitTestDTO.RadiographCatalogDTO dto = new RadiographVisitTestDTO.RadiographCatalogDTO();
        dto.setId(catalogItem.getId());
        dto.setName(catalogItem.getName());
        dto.setType(catalogItem.getType().toString());
        dto.setPrice(catalogItem.getPrice());
        dto.setDescription(catalogItem.getDescription());
        dto.setActive(catalogItem.getActive());
        return dto;
    }
}
