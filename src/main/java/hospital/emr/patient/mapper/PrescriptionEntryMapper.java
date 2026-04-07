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

    // Entity creation: map DTO fields to entity
    @Mapping(source = "prescriptionId", target = "prescription.id")
    @Mapping(source = "itemId", target = "item.id")
    @Mapping(source = "itemName", target = "itemName")  // Correct! Maps to PrescriptionEntry.itemName
    PrescriptionEntry toEntity(PrescriptionEntryDTO dto);

    // DTO creation: IMPORTANT FIX HERE!
    @Mapping(source = "prescription.id", target = "prescriptionId")
    @Mapping(source = "item.id", target = "itemId")
    @Mapping(target = "itemName", expression = "java(getItemName(entity))")  // FIXED!
    PrescriptionEntryDTO toDto(PrescriptionEntry entity);

    // Helper method for the expression
    default String getItemName(PrescriptionEntry entity) {
        // Priority 1: Use entity.itemName if set
        if (entity.getItemName() != null && !entity.getItemName().trim().isEmpty()) {
            return entity.getItemName();
        }
        // Priority 2: Fallback to item.name if item exists
        if (entity.getItem() != null && entity.getItem().getName() != null) {
            return entity.getItem().getName();
        }
        // Priority 3: Return null if neither is available
        return null;
    }

    // Optional: Update mapping for partial updates
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "itemId", target = "item.id")
    @Mapping(source = "itemName", target = "itemName")
    void updateEntityFromDto(PrescriptionEntryDTO dto, @MappingTarget PrescriptionEntry entity);
}