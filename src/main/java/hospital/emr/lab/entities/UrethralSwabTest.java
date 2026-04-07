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
 * Urethral Swab Test
 * Price: 6,000
 * Category: MICROBIOLOGY
 * Sample Type: SWAB
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("URETHRAL_SWAB")
public class UrethralSwabTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: No pathogens isolated
        Common Pathogens: Neisseria gonorrhoeae, Chlamydia trachomatis, Ureaplasma, Mycoplasma
        White Blood Cells: <5 per HPF (Normal), >5 per HPF (Indicates infection)
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("URETHRAL SWAB");
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
