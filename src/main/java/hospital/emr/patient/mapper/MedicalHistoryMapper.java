package hospital.emr.patient.mapper;

import hospital.emr.common.mappers.NoteMapper;
import hospital.emr.patient.dtos.MedicalHistoryDTO;
import hospital.emr.patient.entities.MedicalHistory;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {NoteMapper.class, PrescriptionMapper.class, VitalSignsMapper.class, AdmissionMapper.class, PatientMapper.class})
public interface MedicalHistoryMapper {

    @Mapping(source = "patientId", target = "patient.id")
    @Mapping(target = "patient.names", ignore = true) // Don't map names back to entity
    @Mapping(target = "patient.code", ignore = true)
    @Mapping(target = "diagnosisNotes", ignore = true) // Collections are managed by service layer
    @Mapping(target = "clinicalNotes", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "vitalSignsList", ignore = true)
    @Mapping(target = "admissions", ignore = true)
    MedicalHistory toEntity(MedicalHistoryDTO dto);

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.names", target = "patientNames")
    @Mapping(source = "patient.code", target = "patientCode")
    MedicalHistoryDTO toDto(MedicalHistory entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "diagnosisNotes", ignore = true)
    @Mapping(target = "clinicalNotes", ignore = true)
    @Mapping(target = "prescriptions", ignore = true)
    @Mapping(target = "vitalSignsList", ignore = true)
    @Mapping(target = "admissions", ignore = true)
    void updateFromDto(MedicalHistoryDTO dto, @MappingTarget MedicalHistory entity);
}