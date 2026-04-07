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
 * Stool M/C/S (Microscopy, Culture & Sensitivity) Test
 * Price: 10,000
 * Category: MICROBIOLOGY
 * Sample Type: STOOL
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("STOOL_MCS")
public class StoolMCSTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: No pathogenic organisms isolated
        Common Pathogens: Salmonella, Shigella, Campylobacter, E. coli O157:H7
        Sensitivity: Reported for identified pathogens
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("STOOL M/C/S");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("10000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.MICROBIOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.STOOL);
        }
    }
}
