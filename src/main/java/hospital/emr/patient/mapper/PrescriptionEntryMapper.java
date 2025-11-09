package hospital.emr.patient.mapper;

import hospital.emr.patient.dtos.PrescriptionEntryDTO;
import hospital.emr.patient.entities.PrescriptionEntry;
import hospital.emr.pharmacy.mappers.ItemMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {ItemMapper.class})
public interface PrescriptionEntryMapper {

    // Corrected mapping: ignore back-reference and map prescriptionId to prescription.id
    @Mapping(source = "prescriptionId", target = "prescription.id")
    @Mapping(source = "itemId", target = "item.id")
    @Mapping(source = "itemName", target = "item.name")
    PrescriptionEntry toEntity(PrescriptionEntryDTO dto);

    @Mapping(source = "prescription.id", target = "prescriptionId")
    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.name", target = "itemName")
    PrescriptionEntryDTO toDto(PrescriptionEntry entity);
}