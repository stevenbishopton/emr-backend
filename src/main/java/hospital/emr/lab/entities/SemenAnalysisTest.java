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
 * Semen Analysis Test
 * Price: 5,000
 * Category: REPRODUCTIVE_HEALTH
 * Sample Type: SEMEN
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("SEMEN_ANALYSIS")
public class SemenAnalysisTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Volume: ≥1.5 mL
        Sperm Count: ≥15 million/mL
        Total Motility: ≥40%
        Progressive Motility: ≥32%
        Morphology: ≥4% normal forms
        pH: 7.2-8.0
        Viability: ≥58%
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("SEMEN ANALYSIS");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("5000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.REPRODUCTIVE_HEALTH);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.SEMEN);
        }
    }
}
