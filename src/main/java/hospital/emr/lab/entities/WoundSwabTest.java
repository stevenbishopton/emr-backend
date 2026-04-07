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
 * Wound Swab Test
 * Price: 5,000
 * Category: MICROBIOLOGY
 * Sample Type: SWAB
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("WOUND_SWAB")
public class WoundSwabTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: No pathogens or mixed skin flora only
        Common Pathogens: Staphylococcus aureus, Streptococcus pyogenes, Pseudomonas, E. coli
        Significant: Pure growth or predominant organism
        Sensitivity: Reported for identified pathogens
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("WOUND SWAB");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("5000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.MICROBIOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.SWAB);
        }
    }
}
