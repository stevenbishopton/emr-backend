package hospital.emr.common.mappers;

import hospital.emr.common.dtos.DepartmentDTO;
import hospital.emr.common.entities.Department;
import org.mapstruct.*;


@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DepartmentMapper {

    DepartmentDTO toDto(Department department);


    @Mapping(target = "personnel", ignore = true)
    Department toEntity(DepartmentDTO departmentDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "personnel", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(DepartmentDTO departmentDTO, @MappingTarget Department department);
}
