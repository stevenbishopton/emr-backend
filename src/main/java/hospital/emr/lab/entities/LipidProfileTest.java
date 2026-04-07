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
 * Lipid Profile Test
 * Price: 15,000
 * Category: BIOCHEMISTRY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("LIPID_PROFILE")
public class LipidProfileTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Total Cholesterol: <200 mg/dL (Desirable)
        LDL Cholesterol: <100 mg/dL (Optimal)
        HDL Cholesterol: Male >40 mg/dL, Female >50 mg/dL
        Triglycerides: <150 mg/dL (Normal)
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("LIPID PROFILE");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("15000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.BIOCHEMISTRY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
