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
 * Hepatitis B/C Test (HBsAg; HCV)
 * Price: 3,000
 * Category: INFECTIOUS_DISEASES
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("HEPATITIS_BC")
public class HepatitisBCTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        HBsAg (Hepatitis B Surface Antigen): Negative (Normal), Positive (Indicates HBV infection)
        HCV (Hepatitis C Virus): Negative (Normal), Positive (Indicates HCV infection)
        Note: Both tests are qualitative (Positive/Negative)
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("HEPATITIS B/C (HBsAg; HCV)");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("3000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.INFECTIOUS_DISEASES);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
