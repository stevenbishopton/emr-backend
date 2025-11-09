package hospital.emr.reception.mappers;

import hospital.emr.reception.dtos.VisitDTO;
import hospital.emr.reception.dtos.NewVisitRequest;
import hospital.emr.reception.entities.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import hospital.emr.reception.services.VisitDepartmentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", uses = {VisitDepartmentMapper.class})
public interface VisitMapper {

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.names", target = "patientName")
    @Mapping(source = "patient.code", target = "patientCode")
    @Mapping(source = "visitDateTime", target = "visitDateTime", qualifiedByName = "localDateTimeToString")
    VisitDTO toDto(Visit visit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Visit toEntity(NewVisitRequest newVisitRequest);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(source = "visitDateTime", target = "visitDateTime", qualifiedByName = "stringToLocalDateTime")
    Visit toEntity(VisitDTO visitDto);

    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }

    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String dateTimeString) {
        return dateTimeString != null ? LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null;
    }
}