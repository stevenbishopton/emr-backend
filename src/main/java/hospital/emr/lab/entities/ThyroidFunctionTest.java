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
 * Thyroid Function Test
 * Price: 30,000
 * Category: ENDOCRINOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("THYROID_FUNCTION")
public class ThyroidFunctionTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        TSH (Thyroid Stimulating Hormone): 0.4-4.0 mIU/L
        Free T4 (FT4): 0.8-1.8 ng/dL (10-23 pmol/L)
        Free T3 (FT3): 2.3-4.2 pg/mL (3.5-6.5 pmol/L)
        Total T4: 5.0-12.0 μg/dL (64-154 nmol/L)
        Total T3: 80-200 ng/dL (1.2-3.1 nmol/L)
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("THYROID FUNCTION TEST");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("30000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.ENDOCRINOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
