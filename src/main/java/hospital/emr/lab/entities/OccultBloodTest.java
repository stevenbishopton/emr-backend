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
 * Occult Blood Test
 * Price: 3,000
 * Category: STOOL_ANALYSIS
 * Sample Type: STOOL
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("OCCULT_BLOOD")
public class OccultBloodTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: Negative (No blood detected)
        Positive: Blood detected (May indicate GI bleeding)
        Note: Requires 3 consecutive samples for colorectal cancer screening
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("OCCULT BLOOD");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("3000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.STOOL_ANALYSIS);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.STOOL);
        }
    }
}
