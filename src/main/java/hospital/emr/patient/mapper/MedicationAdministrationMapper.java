package hospital.emr.patient.mapper;

import hospital.emr.patient.dtos.MedicationAdministrationDTO;
import hospital.emr.patient.entities.MedicationAdministration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicationAdministrationMapper {

    @Mapping(source = "prescriptionEntry.id", target = "prescriptionEntryId")
    MedicationAdministrationDTO toDto(MedicationAdministration entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "prescriptionEntry", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    MedicationAdministration toEntity(MedicationAdministrationDTO dto);
}
