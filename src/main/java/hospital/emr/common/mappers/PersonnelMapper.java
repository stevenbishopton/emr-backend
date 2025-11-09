package hospital.emr.common.mappers;

import hospital.emr.admin.entities.Admin;
import hospital.emr.common.dtos.PersonnelDTO;
import hospital.emr.common.entities.*;
import hospital.emr.common.enums.PersonnelType;
import hospital.emr.doctor.entities.Doctor;

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
    @Mapping(target = "department.id", source = "departmentId")
    @Mapping(target = "password", expression = "java(dto.getPassword())")
    Personnel toEntity(PersonnelDTO dto);



    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "department.id", source = "departmentId")
    @Mapping(target = "password", expression = "java(dto.getPassword() != null ? dto.getPassword() : entity.getPassword())")
    void updateFromDto(PersonnelDTO dto, @MappingTarget Personnel entity);


    default PersonnelType determinePersonnelType(Personnel entity) {
        if (entity == null) return null;
        if (entity instanceof Doctor) return PersonnelType.DOCTOR;
        if (entity instanceof Admin) return PersonnelType.NURSE;
        if (entity instanceof Pharmacist) return PersonnelType.PHARMACIST;
        if (entity instanceof Receptionist) return PersonnelType.RECEPTIONIST;
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
            case NURSE -> new Admin();
            case PHARMACIST -> new Pharmacist();
            case RECEPTIONIST -> new Receptionist();
            default -> throw new IllegalArgumentException("Unknown PersonnelType: " + dto.getPersonnelType());
        };
    }

}
