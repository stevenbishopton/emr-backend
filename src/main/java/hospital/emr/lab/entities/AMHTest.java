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
 * AMH (Anti-Müllerian Hormone) Test
 * Price: 40,000
 * Category: ENDOCRINOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("AMH")
public class AMHTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Age 25-29: 1.5-4.0 ng/mL
        Age 30-34: 1.0-3.0 ng/mL
        Age 35-39: 0.5-2.0 ng/mL
        Age 40-44: 0.3-1.5 ng/mL
        Age 45+: <0.5 ng/mL
        Note: Used to assess ovarian reserve
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("AMH");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("40000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.ENDOCRINOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
