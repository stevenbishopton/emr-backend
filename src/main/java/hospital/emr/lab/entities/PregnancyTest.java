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
 * Pregnancy Test (PT)
 * Price: 3,000
 * Category: REPRODUCTIVE_HEALTH
 * Sample Type: URINE
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("PREGNANCY")
public class PregnancyTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Negative: No hCG detected
        Positive: hCG detected (≥25 mIU/mL)
        Note: Can detect pregnancy as early as 7-10 days after conception
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("PREGNANCY TEST (PT)");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("3000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.REPRODUCTIVE_HEALTH);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.URINE);
        }
    }
}
