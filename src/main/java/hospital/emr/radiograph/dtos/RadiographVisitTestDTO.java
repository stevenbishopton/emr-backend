package hospital.emr.radiograph.dtos;

import hospital.emr.radiograph.enums.RadiographType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RadiographVisitTestDTO {

    private Long id;
    
    private Long visitHistoryId;
    
    private RadiographCatalogDTO catalogItem;
    
    private RadiographType type;
    
    private String testName;
    
    private String description;
    
    private BigDecimal price;
    
    private String status;
    
    private String findings;
    
    private String impression;
    
    private String recommendation;
    
    private String imageUrl;
    
    private String reportUrl;
    
    private LocalDateTime performedAt;
    
    private LocalDateTime reportedAt;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private Long version;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RadiographCatalogDTO {
        private Long id;
        private String name;
        private String type;
        private BigDecimal price;
        private String description;
        private Boolean active;
    }
}
