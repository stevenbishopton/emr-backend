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
 * Fasting Serum Lipid Profile (FLP)
 * Price: 15,000
 * Category: BIOCHEMISTRY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("FLP")
public class FLPTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Total Cholesterol: <200 mg/dL (Desirable), 200-239 mg/dL (Borderline), ≥240 mg/dL (High)
        LDL Cholesterol: <100 mg/dL (Optimal), 100-129 mg/dL (Near optimal), ≥130 mg/dL (High)
        HDL Cholesterol: Male >40 mg/dL, Female >50 mg/dL (Higher is better)
        Triglycerides: <150 mg/dL (Normal), 150-199 mg/dL (Borderline), ≥200 mg/dL (High)
        Note: Requires 12-hour fasting
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("FASTING SERUM LIPID PROFILE (FLP)");
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
