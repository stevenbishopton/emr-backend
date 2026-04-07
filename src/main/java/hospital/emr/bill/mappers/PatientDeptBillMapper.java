package hospital.emr.bill.mappers;

import hospital.emr.bill.dtos.PatientDeptBillDto;
import hospital.emr.bill.entities.PatientDeptBill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PatientDeptBillMapper.class})
public interface PatientDeptBillMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeIssued", ignore = true)
    PatientDeptBill toEntity(PatientDeptBillDto dto);

    PatientDeptBillDto toDto(PatientDeptBill entity);

    List<PatientDeptBillDto> toDtoList(List<PatientDeptBill> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeIssued", ignore = true)
    void updateEntityFromDto(PatientDeptBillDto dto, @org.mapstruct.MappingTarget PatientDeptBill entity);
}