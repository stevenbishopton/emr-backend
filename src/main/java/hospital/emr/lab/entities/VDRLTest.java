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
 * VDRL/Khan Test (Syphilis Screening)
 * Price: 4,000
 * Category: INFECTIOUS_DISEASES
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("VDRL")
public class VDRLTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: Non-reactive (Negative)
        Reactive: Positive (Requires confirmation with TPHA/FTA-ABS)
        Titers: Reported as 1:1, 1:2, 1:4, 1:8, 1:16, etc.
        Note: Screening test for syphilis
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("VDRL/KHAN TEST");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("4000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.INFECTIOUS_DISEASES);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
