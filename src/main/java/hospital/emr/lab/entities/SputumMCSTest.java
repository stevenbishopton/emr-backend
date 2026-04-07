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
 * Sputum M/C/S (Microscopy, Culture & Sensitivity) Test
 * Price: 10,000
 * Category: MICROBIOLOGY
 * Sample Type: SPUTUM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("SPUTUM_MCS")
public class SputumMCSTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: Mixed oral flora or no growth
        Common Pathogens: Streptococcus pneumoniae, Haemophilus influenzae, Klebsiella, Pseudomonas
        Significant: >10^6 CFU/mL or predominant organism
        Sensitivity: Reported for identified pathogens
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("SPUTUM M/C/S");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("10000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.MICROBIOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.SPUTUM);
        }
    }
}
