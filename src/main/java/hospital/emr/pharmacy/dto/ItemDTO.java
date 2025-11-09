package hospital.emr.pharmacy.dto;

import hospital.emr.pharmacy.enums.ItemType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ItemDTO {
    private Long id;
    private ItemType itemType;
    private String name;
    private LocalDate expirationDate;
    private BigDecimal quantity;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private String description;
    private String link;
}
