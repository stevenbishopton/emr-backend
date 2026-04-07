package hospital.emr.common.mappers;

import hospital.emr.admin.entities.Admin;
import hospital.emr.common.dtos.PersonnelDTO;
import hospital.emr.common.entities.Department;
import hospital.emr.common.entities.Personnel;
import hospital.emr.common.enums.PersonnelType;
import hospital.emr.doctor.entities.Doctor;
import hospital.emr.nurse.entities.Nurse;
import hospital.emr.pharmacy.entities.Pharmacist;
import hospital.emr.reception.entities.Receptionist;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonnelMapper {

    // Convert Entity -> DTO
    @Mapping(target = "personnelType", expression = "java(determinePersonnelType(entity))")
    @Mapping(target = "departmentId", source = "department.id")
    PersonnelDTO toDto(Personnel entity);

    // Convert DTO -> Entity (delegate to factory)
    @Mapping(target = "department", expression = "java(mapDepartment(dto.getDepartmentId()))")
    @Mapping(target = "password", expression = "java(dto.getPassword())")
    Personnel toEntity(PersonnelDTO dto);



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "department", expression = "java(mapDepartment(dto.getDepartmentId()))")
    @Mapping(target = "password", expression = "java(dto.getPassword() != null ? dto.getPassword() : entity.getPassword())")
    void updateFromDto(PersonnelDTO dto, @MappingTarget Personnel entity);


    default PersonnelType determinePersonnelType(Personnel entity) {
        if (entity == null) return null;
        if (entity instanceof Doctor) return PersonnelType.DOCTOR;
        if (entity instanceof Admin) return PersonnelType.ADMIN;
        if (entity instanceof Nurse) return PersonnelType.NURSE;
        if (entity instanceof Pharmacist) return PersonnelType.PHARMACIST;
        if (entity instanceof Receptionist) return PersonnelType.RECEPTIONIST;
//        if (entity instanceof LabPersonnel) return PersonnelType.LAB_PERSONNEL;
        return null;
    }

    /**
     * Factory method: creates the right subclass instance based on PersonnelType
     */
    @ObjectFactory
    default Personnel resolveEntity(PersonnelDTO dto) {
        if (dto == null || dto.getPersonnelType() == null) return null;

        return switch (dto.getPersonnelType()) {
            case DOCTOR -> new Doctor();
            case ADMIN -> new Admin();
            case NURSE -> new Nurse();
            case PHARMACIST -> new Pharmacist();
            case RECEPTIONIST -> new Receptionist();
//            case LAB_PERSONNEL -> new LabPersonnel();
            default -> throw new IllegalArgumentException("Unknown PersonnelType: " + dto.getPersonnelType());
        };
    }

    default Department mapDepartment(Long departmentId) {
        if (departmentId == null) {
            return null;
        }
        Department department = new Department();
        department.setId(departmentId);
        return department;
    }

}
