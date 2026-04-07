package hospital.emr.radiograph.mappers;

import hospital.emr.radiograph.dtos.RadiographHistoryDTO;
import hospital.emr.radiograph.entities.RadiographHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RadiographHistoryMapper {
    
    RadiographHistoryDTO toDto(RadiographHistory entity);
    
    RadiographHistory toEntity(RadiographHistoryDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    RadiographHistory createEntity(RadiographHistoryDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    void updateEntityFromDto(RadiographHistoryDTO dto, @MappingTarget RadiographHistory entity);
}
