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
 * Bilirubin Total & Direct Test
 * Price: 6,000
 * Category: BIOCHEMISTRY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("BILIRUBIN")
public class BilirubinTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Total Bilirubin: 0.1-1.2 mg/dL (1.7-20.5 μmol/L)
        Direct (Conjugated) Bilirubin: 0.0-0.3 mg/dL (0-5.1 μmol/L)
        Indirect (Unconjugated) Bilirubin: 0.1-0.9 mg/dL (1.7-15.4 μmol/L)
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("BILIRUBIN TOTAL & DIRECT");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("6000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.BIOCHEMISTRY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
