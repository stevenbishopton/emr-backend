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
 * Urine M/C/S (Microscopy, Culture & Sensitivity) Test
 * Price: 6,000
 * Category: URINALYSIS
 * Sample Type: URINE
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("URINE_MCS")
public class UrineMCSTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: No significant growth (<10^3 CFU/mL)
        Significant: >10^5 CFU/mL (Indicates infection)
        Common Pathogens: E. coli, Klebsiella, Enterococcus, Proteus
        Sensitivity: Reported for identified organisms
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("URINE M/C/S");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("6000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.URINALYSIS);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.URINE);
        }
    }
}
