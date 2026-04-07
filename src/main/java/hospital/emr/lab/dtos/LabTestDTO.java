package hospital.emr.lab.dtos;

import hospital.emr.lab.enums.SampleType;
import hospital.emr.lab.enums.TestCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for LabTest entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabTestDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private TestCategory category;
    private SampleType sampleType;
    private String description;
    private String referenceRange;
}
