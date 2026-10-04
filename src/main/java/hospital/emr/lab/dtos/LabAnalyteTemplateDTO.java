package hospital.emr.lab.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabAnalyteTemplateDTO {
    private Long id;
    private Long testId;
    private String analyteName;
    private String unit;
    private BigDecimal refLow;
    private BigDecimal refHigh;
    private BigDecimal criticalLow;
    private BigDecimal criticalHigh;
    private Integer displayOrder;
    private Boolean active;
}
