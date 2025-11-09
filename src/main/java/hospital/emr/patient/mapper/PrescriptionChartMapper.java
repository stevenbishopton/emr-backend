package hospital.emr.patient.mapper;

import hospital.emr.patient.dtos.PrescriptionChartDTO;
import hospital.emr.patient.entities.PrescriptionChart;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {AdmissionPrescriptionEntryMapper.class})
public interface PrescriptionChartMapper {

    @Mapping(source = "admission.id", target = "admissionId")
    PrescriptionChartDTO toDto(PrescriptionChart entity);

    @Mapping(source = "admissionId", target = "admission.id")
    PrescriptionChart toEntity(PrescriptionChartDTO dto);
}