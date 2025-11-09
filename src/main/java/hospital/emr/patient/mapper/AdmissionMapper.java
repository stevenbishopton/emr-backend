package hospital.emr.patient.mapper;

import hospital.emr.common.mappers.NoteMapper;
import hospital.emr.patient.dtos.AdmissionDTO;
import hospital.emr.patient.entities.Admission;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {NoteMapper.class})
public interface AdmissionMapper {

    @Mapping(source = "ward.id", target = "wardId")
    @Mapping(source = "ward.name", target = "wardName")
    @Mapping(source = "medicalHistory.id", target = "medicalHistoryId")
    @Mapping(source = "medicalHistory.patient.id", target = "patientId")
    @Mapping(source = "medicalHistory.patient.names", target = "patientNames")
    @Mapping(source = "medicalHistory.patient.code", target = "patientCode")
    @Mapping(source = "visit.id", target = "visitId")
    @Mapping(source = "admissionRecord", target = "admissionRecord")
    AdmissionDTO toDto(Admission entity);

    // Simple toEntity - we'll handle complex mapping in service
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "visit", ignore = true)
    @Mapping(target = "medicalHistory", ignore = true)
    @Mapping(target = "ward", ignore = true)
    @Mapping(target = "admissionRecord", ignore = true)
    @Mapping(target = "prescriptionChart", ignore = true)
    @Mapping(target = "notes", ignore = true)
    Admission toEntity(AdmissionDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "visit", ignore = true)
    @Mapping(target = "medicalHistory", ignore = true)
    @Mapping(target = "ward", ignore = true)
    @Mapping(target = "admissionRecord", ignore = true)
    void updateAdmissionFromDto(AdmissionDTO dto, @MappingTarget Admission entity);
}