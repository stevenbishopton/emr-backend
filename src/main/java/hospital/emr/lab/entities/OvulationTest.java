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
 * Ovulation Test
 * Price: 3,000
 * Category: REPRODUCTIVE_HEALTH
 * Sample Type: URINE
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("OVULATION")
public class OvulationTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Negative: No LH surge detected
        Positive: LH surge detected (Ovulation expected within 24-36 hours)
        Note: Detects luteinizing hormone (LH) surge that precedes ovulation
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("OVULATION TEST");
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
