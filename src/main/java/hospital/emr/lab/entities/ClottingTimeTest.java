package hospital.emr.lab.entities;

import hospital.emr.lab.enums.SampleType;
import hospital.emr.lab.enums.TestCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Clotting Time / Bleeding Time Test
 * Price: 5,000
 * Category: HEMATOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("CLOTTING_TIME")
public class ClottingTimeTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Clotting Time: 5-15 minutes
        Bleeding Time: 2-7 minutes
        Prothrombin Time (PT): 11-13.5 seconds
        Activated Partial Thromboplastin Time (APTT): 25-35 seconds
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("CLOTTING TIME/BLEEDING TIME");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("5000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.HEMATOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
