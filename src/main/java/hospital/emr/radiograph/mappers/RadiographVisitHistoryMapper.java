package hospital.emr.radiograph.mappers;

import hospital.emr.radiograph.dtos.RadiographVisitHistoryDTO;
import hospital.emr.radiograph.dtos.RadiographVisitTestDTO;
import hospital.emr.radiograph.entities.RadiographVisitHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RadiographVisitHistoryMapper {

    @Mapping(target = "patient", expression = "java(mapToPatientDTO(entity.getPatient()))")
    @Mapping(target = "visit", expression = "java(mapToVisitDTO(entity.getVisit()))")
    @Mapping(target = "requestedBy", expression = "java(mapToPersonnelDTO(entity.getRequestedBy()))")
    @Mapping(target = "performedBy", expression = "java(mapToPersonnelDTO(entity.getPerformedBy()))")
    @Mapping(target = "radiologist", expression = "java(mapToPersonnelDTO(entity.getRadiologist()))")
    @Mapping(target = "testsPerformed", expression = "java(mapToTestsDTO(entity.getTestsPerformed()))")
    RadiographVisitHistoryDTO toDto(RadiographVisitHistory entity);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "visit", ignore = true)
    @Mapping(target = "requestedBy", ignore = true)
    @Mapping(target = "performedBy", ignore = true)
    @Mapping(target = "radiologist", ignore = true)
    @Mapping(target = "testsPerformed", ignore = true)
    RadiographVisitHistory toEntity(RadiographVisitHistoryDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "visit", ignore = true)
    @Mapping(target = "requestedBy", ignore = true)
    @Mapping(target = "performedBy", ignore = true)
    @Mapping(target = "radiologist", ignore = true)
    @Mapping(target = "testsPerformed", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    RadiographVisitHistory createEntity(RadiographVisitHistoryDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "visit", ignore = true)
    @Mapping(target = "requestedBy", ignore = true)
    @Mapping(target = "performedBy", ignore = true)
    @Mapping(target = "radiologist", ignore = true)
    @Mapping(target = "testsPerformed", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDto(RadiographVisitHistoryDTO dto, @MappingTarget RadiographVisitHistory entity);

    List<RadiographVisitHistoryDTO> toDtoList(List<RadiographVisitHistory> entities);

    // Helper mapping methods
    @Named("mapToPatientDTO")
    default RadiographVisitHistoryDTO.PatientDTO mapToPatientDTO(hospital.emr.patient.entities.Patient patient) {
        if (patient == null) return null;
        RadiographVisitHistoryDTO.PatientDTO dto = new RadiographVisitHistoryDTO.PatientDTO();
        dto.setId(patient.getId());
        dto.setFirstName(patient.getNames());
        dto.setLastName("");
        dto.setFullName(patient.getNames());
        dto.setDateOfBirth(patient.getDateOfBirth().toString());
        dto.setSex(patient.getSex().toString());
        dto.setPhoneNumber(patient.getPhoneNumber());
        return dto;
    }

    @Named("mapToVisitDTO")
    default RadiographVisitHistoryDTO.VisitDTO mapToVisitDTO(hospital.emr.reception.entities.Visit visit) {
        if (visit == null) return null;
        RadiographVisitHistoryDTO.VisitDTO dto = new RadiographVisitHistoryDTO.VisitDTO();
        dto.setId(visit.getId());
        dto.setVisitDate(visit.getVisitDateTime());
        dto.setVisitType(visit.getStatus().toString());
        dto.setComplaint(visit.getNotes());
        return dto;
    }

    @Named("mapToPersonnelDTO")
    default RadiographVisitHistoryDTO.PersonnelDTO mapToPersonnelDTO(hospital.emr.common.entities.Personnel personnel) {
        if (personnel == null) return null;
        RadiographVisitHistoryDTO.PersonnelDTO dto = new RadiographVisitHistoryDTO.PersonnelDTO();
        dto.setId(personnel.getId());
        dto.setNames(personnel.getNames());
        dto.setPersonnelType(personnel.getClass().getSimpleName());
        dto.setDepartment("Radiology"); // Default or get from relationship
        return dto;
    }

    @Named("mapToTestsDTO")
    default List<RadiographVisitTestDTO> mapToTestsDTO(List<hospital.emr.radiograph.entities.RadiographVisitTest> tests) {
        if (tests == null) return null;
        return tests.stream()
                .map(this::mapToTestDTO)
                .toList();
    }

    @Named("mapToTestDTO")
    default RadiographVisitTestDTO mapToTestDTO(hospital.emr.radiograph.entities.RadiographVisitTest test) {
        if (test == null) return null;
        RadiographVisitTestDTO dto = new RadiographVisitTestDTO();
        dto.setId(test.getId());
        dto.setVisitHistoryId(test.getVisitHistory() != null ? test.getVisitHistory().getId() : null);
        dto.setType(test.getType());
        dto.setTestName(test.getTestName());
        dto.setDescription(test.getDescription());
        dto.setPrice(test.getPrice());
        dto.setStatus(test.getStatus());
        dto.setFindings(test.getFindings());
        dto.setImpression(test.getImpression());
        dto.setRecommendation(test.getRecommendation());
        dto.setImageUrl(test.getImageUrl());
        dto.setReportUrl(test.getReportUrl());
        dto.setPerformedAt(test.getPerformedAt());
        dto.setReportedAt(test.getReportedAt());
        dto.setCreatedAt(test.getCreatedAt());
        dto.setUpdatedAt(test.getUpdatedAt());
        dto.setVersion(test.getVersion());
        
        // Map catalog item if present
        if (test.getCatalogItem() != null) {
            RadiographVisitTestDTO.RadiographCatalogDTO catalogDto = new RadiographVisitTestDTO.RadiographCatalogDTO();
            catalogDto.setId(test.getCatalogItem().getId());
            catalogDto.setName(test.getCatalogItem().getName());
            catalogDto.setType(test.getCatalogItem().getType().toString());
            catalogDto.setPrice(test.getCatalogItem().getPrice());
            catalogDto.setDescription(test.getCatalogItem().getDescription());
            catalogDto.setActive(test.getCatalogItem().getActive());
            dto.setCatalogItem(catalogDto);
        }
        
        return dto;
    }
}
