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
 * Urinalysis Test
 * Price: 2,000
 * Category: URINALYSIS
 * Sample Type: URINE
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("URINALYSIS")
public class UrinalysisTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Color: Yellow to amber
        Appearance: Clear
        pH: 4.5-8.0
        Specific Gravity: 1.005-1.030
        Protein: Negative or trace
        Glucose: Negative
        Ketones: Negative
        Blood: Negative
        Leukocytes: Negative
        Nitrites: Negative
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("URINALYSIS");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("2000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.URINALYSIS);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.URINE);
        }
    }
}
