package hospital.emr.patient.mapper;

import hospital.emr.patient.dtos.PatientSimpleDTO;
import hospital.emr.patient.entities.Patient;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PatientSimpleMapper {
    Patient toEntity(PatientSimpleDTO dto);
    PatientSimpleDTO toDto(Patient entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(PatientSimpleDTO dto, @MappingTarget Patient entity);
}
