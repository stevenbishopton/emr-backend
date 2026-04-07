package hospital.emr.radiograph.dtos;

import hospital.emr.radiograph.enums.RadiographType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class RadiographCatalogDTO {
    private Long id;
    private String name;
    private RadiographType type;
    private BigDecimal price;
    private String description;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}
