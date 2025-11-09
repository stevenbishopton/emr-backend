package hospital.emr.patient.mapper;

import hospital.emr.patient.dtos.AdmissionPrescriptionEntryDTO;
import hospital.emr.patient.entities.AdmissionPrescriptionEntry;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PrescriptionAdministrationMapper.class})
public interface AdmissionPrescriptionEntryMapper {

    @Mapping(source = "chart.admission.id", target = "admissionId")
    @Mapping(source = "chart.id", target = "prescriptionChartId")
    AdmissionPrescriptionEntryDTO toDto(AdmissionPrescriptionEntry entry);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "chart", ignore = true)
    @Mapping(target = "administrations", ignore = true)
    @Mapping(source = "prescriptionChartId", target = "chart.id")
    AdmissionPrescriptionEntry toEntity(AdmissionPrescriptionEntryDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "chart", ignore = true)
    @Mapping(target = "administrations", ignore = true)
    @Mapping(source = "prescriptionChartId", target = "chart.id")
    void updateEntityFromDto(AdmissionPrescriptionEntryDTO dto, @MappingTarget AdmissionPrescriptionEntry entity);
}