package hospital.emr.patient.mapper;

import hospital.emr.patient.dtos.PrescriptionAdministrationDTO;
import hospital.emr.patient.entities.PrescriptionAdministration;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PrescriptionAdministrationMapper {

    @Mapping(source = "prescriptionEntry.id", target = "prescriptionEntryId")
    PrescriptionAdministrationDTO toDto(PrescriptionAdministration administration);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescriptionEntry", ignore = true)
    @Mapping(source = "prescriptionEntryId", target = "prescriptionEntry.id")
    PrescriptionAdministration toEntity(PrescriptionAdministrationDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescriptionEntry", ignore = true)
    @Mapping(source = "prescriptionEntryId", target = "prescriptionEntry.id")
    void updateEntityFromDto(PrescriptionAdministrationDTO dto, @MappingTarget PrescriptionAdministration entity);
}