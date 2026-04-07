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
 * High Vagina Swab (HVS) Test
 * Price: 6,000
 * Category: MICROBIOLOGY
 * Sample Type: SWAB
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("HVS")
public class HVSTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal Flora: Lactobacilli present
        Common Pathogens: Candida albicans, Trichomonas vaginalis, Gardnerella vaginalis
        White Blood Cells: <10 per HPF (Normal), >10 per HPF (Indicates infection)
        Clue Cells: Absent (Normal), Present (Bacterial vaginosis)
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("HIGH VAGINA SWAB (HVS)");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("6000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.MICROBIOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.SWAB);
        }
    }
}
