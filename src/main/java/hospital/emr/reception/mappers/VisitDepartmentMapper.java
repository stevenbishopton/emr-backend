package hospital.emr.reception.mappers;

import hospital.emr.reception.dtos.VisitDepartmentDTO;
import hospital.emr.reception.entities.VisitDepartment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitDepartmentMapper {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "visit.id", target = "visitId")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "visit.patient.id", target = "patientId")
    @Mapping(source = "visit.patient.names", target = "patientName")
    @Mapping(source = "visit.patient.code", target = "patientCode")
    @Mapping(source = "visit.visitDateTime", target = "visitDateTime")
    VisitDepartmentDTO toDto(VisitDepartment visitDepartment);
}