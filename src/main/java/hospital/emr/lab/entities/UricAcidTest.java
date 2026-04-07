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
 * Uric Acid Test
 * Price: 15,000
 * Category: BIOCHEMISTRY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("URIC_ACID")
public class UricAcidTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Adult Male: 3.4-7.0 mg/dL (202-416 μmol/L)
        Adult Female: 2.4-6.0 mg/dL (143-357 μmol/L)
        Children: 2.0-5.5 mg/dL (119-327 μmol/L)
        Note: Elevated levels may indicate gout or kidney disease
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("URIC ACID");
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
