package hospital.emr.bill.mappers;

import hospital.emr.bill.dtos.SubBillDTO;
import hospital.emr.bill.entities.SubBill;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubBillMapper {

    SubBillDTO toDTO(SubBill subBill);
    SubBill toEntity(SubBillDTO subBillDTO);

    List<SubBillDTO> toDTOList(List<SubBill> subBills);
    List<SubBill> toEntityList(List<SubBillDTO> subBillDTOs);
}
