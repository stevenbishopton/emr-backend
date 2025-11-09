package hospital.emr.patient.mapper;

import hospital.emr.bill.mappers.BillMapper;
import hospital.emr.patient.dtos.PatientDTO;
import hospital.emr.patient.entities.Patient;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {BillMapper.class})
public interface PatientMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "bills", ignore = true)
    Patient toEntity(PatientDTO patientDTO);

    @Mapping(source = "medicalHistory.id", target = "medicalHistoryId")
    PatientDTO toDto(Patient patient);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "medicalHistory", ignore = true) // Keep this for updates to prevent overwriting existing MedicalHistory
    @Mapping(target = "bills", ignore = true)
    void updatePatientFromDto(PatientDTO patientDTO, @MappingTarget Patient patient);
}
