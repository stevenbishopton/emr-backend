package hospital.emr.reception.mappers;

import hospital.emr.reception.dtos.VisitDepartmentDTO;
import hospital.emr.reception.entities.VisitDepartment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitDepartmentMapper {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "visit.id", target = "visitId")
    @Mapping(source = "status", target = "status")   // 👈 explicitly include
    @Mapping(source = "notes", target = "notes")
    @Mapping(source = "department.name", target = "departmentName")
    VisitDepartmentDTO toDto(VisitDepartment visitDepartment);
}