package hospital.emr.patient.mapper;

import hospital.emr.patient.dtos.PrescriptionDTO;
import hospital.emr.patient.entities.Prescription;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PrescriptionEntryMapper.class})
public interface PrescriptionMapper {

    @Mapping(source = "medicalHistoryId", target = "medicalHistory.id")
    @Mapping(source = "visitId", target = "visit.id")
    Prescription toEntity(PrescriptionDTO dto);

    @Mapping(source = "medicalHistory.id", target = "medicalHistoryId")
    @Mapping(source = "visit.id", target = "visitId")
    PrescriptionDTO toDto(Prescription entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "medicalHistoryId", target = "medicalHistory.id")
    @Mapping(source = "visitId", target = "visit.id")
    @Mapping(target = "prescriptionEntries", ignore = true)
    void updateFromDto(PrescriptionDTO dto, @MappingTarget Prescription entity);
}