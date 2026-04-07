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
 * PSA (Prostate-Specific Antigen) Test
 * Price: 23,000
 * Category: ENDOCRINOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("PSA")
public class PSATest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Age 40-49: <2.5 ng/mL
        Age 50-59: <3.5 ng/mL
        Age 60-69: <4.5 ng/mL
        Age 70+: <6.5 ng/mL
        Note: Elevated levels may indicate prostate cancer or benign prostatic hyperplasia
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("PSA");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("23000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.ENDOCRINOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
