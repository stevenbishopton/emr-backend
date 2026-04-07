package hospital.emr.radiograph.mappers;

import hospital.emr.radiograph.dtos.RadiographDTO;
import hospital.emr.radiograph.entities.Radiograph;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RadiographMapper {

    RadiographDTO toDto(Radiograph entity);

    Radiograph toEntity(RadiographDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Radiograph createEntity(RadiographDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(RadiographDTO dto, @MappingTarget Radiograph entity);
}
