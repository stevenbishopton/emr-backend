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
 * Pint of Blood (Screening) RH Negative
 * Price: 50,000
 * Category: BLOOD_BANKING
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("BLOOD_SCREENING_RH_NEGATIVE")
public class BloodScreeningRHNegativeTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Screening includes: HIV, Hepatitis B, Hepatitis C, Syphilis, Malaria
        All tests must be: Negative (Non-reactive)
        Blood Unit: 1 pint (450-500 mL)
        Rh Type: Negative
        Note: Complete blood screening for transfusion safety (Rare blood type)
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("PINT OF BLOOD (SCREENING) RH NEGATIVE");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("50000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.BLOOD_BANKING);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
