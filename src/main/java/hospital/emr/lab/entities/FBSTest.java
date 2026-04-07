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
 * Fasting Blood Sugar (FBS) Test
 * Price: 2,000
 * Category: BIOCHEMISTRY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("FBS")
public class FBSTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: 70-100 mg/dL (3.9-5.5 mmol/L)
        Prediabetes: 100-125 mg/dL (5.6-6.9 mmol/L)
        Diabetes: ≥126 mg/dL (≥7.0 mmol/L)
        Note: Requires 8-12 hours fasting before test
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("FASTING BLOOD SUGAR (FBS)");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("2000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.BIOCHEMISTRY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
