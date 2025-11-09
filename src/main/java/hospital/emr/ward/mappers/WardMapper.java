package hospital.emr.ward.mappers;

import hospital.emr.ward.dtos.WardDTO;
import hospital.emr.ward.entities.Ward;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WardMapper {
    WardDTO toDto(Ward ward);

    @Mapping(target = "admissions", ignore = true)
    @Mapping(target = "id", ignore = true)
    Ward toEntity(WardDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateWardFromDto(WardDTO dto, @MappingTarget Ward entity);
}
