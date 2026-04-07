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
 * Chlamydia Test
 * Price: 3,000
 * Category: INFECTIOUS_DISEASES
 * Sample Type: SWAB
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("CHLAMYDIA")
public class ChlamydiaTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: Negative (No Chlamydia trachomatis detected)
        Positive: Chlamydia trachomatis detected
        Note: Can be performed on urine, swab, or cervical sample
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("CHLAMYDIA TEST");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("3000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.INFECTIOUS_DISEASES);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.SWAB);
        }
    }
}
