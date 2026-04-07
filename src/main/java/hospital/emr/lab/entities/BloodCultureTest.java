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
 * Blood Culture Test
 * Price: 35,000
 * Category: MICROBIOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("BLOOD_CULTURE")
public class BloodCultureTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: No growth after 5 days
        Common Pathogens: Staphylococcus aureus, E. coli, Klebsiella, Streptococcus, Enterococcus
        Positive: Growth detected (usually within 24-48 hours)
        Sensitivity: Reported for identified organisms
        Note: Usually requires 2-3 sets from different sites
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("BLOOD CULTURE");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("35000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.MICROBIOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
