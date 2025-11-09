package hospital.emr.pharmacy.mappers;

import hospital.emr.pharmacy.dto.ItemDTO;
import hospital.emr.pharmacy.entities.Item;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {

    ItemDTO toDto(Item item);

    @Mapping(target = "id", ignore = true)
    Item toEntity(ItemDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(ItemDTO dto, @MappingTarget Item entity);
}