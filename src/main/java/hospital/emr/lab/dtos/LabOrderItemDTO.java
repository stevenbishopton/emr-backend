package hospital.emr.lab.dtos;

import hospital.emr.lab.enums.LabOrderStatus;
import hospital.emr.lab.enums.SampleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabOrderItemDTO {
    private Long id;
    private Long testId;
    private String testName;
    private SampleType sampleType;
    private BigDecimal price;
    private LabOrderStatus status;
    private Long specimenId;
    private String performedByName;
    private LocalDateTime performedAt;
    private String resultedByName;
    private LocalDateTime resultedAt;
    private String notes;
    private List<LabAnalyteResultDTO> analyteResults;
}
