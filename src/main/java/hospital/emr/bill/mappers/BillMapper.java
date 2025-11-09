package hospital.emr.bill.mappers;

import hospital.emr.bill.dtos.BillDTO;
import hospital.emr.bill.entities.Bill;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SubBillMapper.class})
public interface BillMapper {

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientNames", source = "patient.names")
    BillDTO toDto(Bill bill);

    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "id", ignore = true) // Add this to ignore ID during creation
    @Mapping(target = "createdAt", ignore = true) // Let database handle this
    Bill toEntity(BillDTO billDTO);

    List<BillDTO> toDtoList(List<Bill> bills);
    List<Bill> toEntityList(List<BillDTO> billDTOs);
}