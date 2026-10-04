package hospital.emr.lab.dtos;

import hospital.emr.lab.enums.ResultFlag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabAnalyteResultDTO {
    private Long id;
    private String analyteName;
    private BigDecimal valueNumeric;
    private String valueText;
    private String unit;
    private BigDecimal refLow;
    private BigDecimal refHigh;
    private BigDecimal criticalLow;
    private BigDecimal criticalHigh;
    private ResultFlag flag;
    private String method;
    private String comment;
}
