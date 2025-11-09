package hospital.emr.patient.mapper;

import hospital.emr.patient.dtos.VitalSignsDTO;
import hospital.emr.patient.entities.VitalSigns;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VitalSignsMapper {

    // ----------------------------- TO ENTITY --------------------------------
    @Mapping(source = "medicalHistoryId", target = "medicalHistory.id")
    @Mapping(source = "visitId", target = "visit.id")  // ✅ Added
    VitalSigns toEntity(VitalSignsDTO dto);

    // ----------------------------- TO DTO --------------------------------
    @Mapping(source = "medicalHistory.id", target = "medicalHistoryId")
    @Mapping(source = "visit.id", target = "visitId")  // ✅ Added
    VitalSignsDTO toDto(VitalSigns entity);
}
