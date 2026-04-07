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
 * Semen M/C/S (Microscopy, Culture & Sensitivity) Test
 * Price: 5,000
 * Category: MICROBIOLOGY
 * Sample Type: SEMEN
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("SEMEN_MCS")
public class SemenMCSTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: No significant growth
        Common Pathogens: E. coli, Enterococcus, Staphylococcus, Ureaplasma
        Significant: >10^3 CFU/mL
        Sensitivity: Reported for identified organisms
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("SEMEN M/C/S");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("5000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.MICROBIOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.SEMEN);
        }
    }
}
