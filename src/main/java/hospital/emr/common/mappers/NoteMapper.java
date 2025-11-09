package hospital.emr.common.mappers;

import hospital.emr.common.dtos.NoteDTO;
import hospital.emr.common.entities.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NoteMapper {

    @Mapping(source = "visit.id", target = "visitId")
    @Mapping(source = "medicalHistory.id", target = "medicalHistoryId")
    NoteDTO toDto(Note note);

    @Mapping(source = "visitId", target = "visit.id")
    @Mapping(source = "medicalHistoryId", target = "medicalHistory.id")
    Note toEntity(NoteDTO noteDTO);
}