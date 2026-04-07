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
 * Electrolyte, Urea, Creatinine (E/U/Cr) Test
 * Price: 15,000
 * Category: BIOCHEMISTRY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("EUCCR")
public class EUCCrTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Sodium (Na+): 136-145 mEq/L
        Potassium (K+): 3.5-5.0 mEq/L
        Chloride (Cl-): 98-107 mEq/L
        Bicarbonate (HCO3-): 22-28 mEq/L
        Urea: 7-20 mg/dL (2.5-7.1 mmol/L)
        Creatinine: Male 0.7-1.3 mg/dL, Female 0.6-1.1 mg/dL
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("ELECTROLYTE, UREA, CREATININE (E/U/Cr)");
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
