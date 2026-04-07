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
 * Liver Function Test (LFT)
 * Price: 15,000
 * Category: BIOCHEMISTRY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("LFT")
public class LFTTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        ALT (Alanine Aminotransferase): 7-56 U/L
        AST (Aspartate Aminotransferase): 10-40 U/L
        ALP (Alkaline Phosphatase): 44-147 U/L
        Total Bilirubin: 0.1-1.2 mg/dL
        Direct Bilirubin: 0.0-0.3 mg/dL
        Total Protein: 6.0-8.3 g/dL
        Albumin: 3.5-5.0 g/dL
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("LIVER FUNCTION TEST (LFT)");
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
