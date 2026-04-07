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
 * Widal Test (Typhoid Fever Test)
 * Price: 3,000
 * Category: INFECTIOUS_DISEASES
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("WIDAL")
public class WidalTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: ≤1:80
        Suspicious: 1:160
        Significant: 1:320
        Diagnostic: ≥1:640
        Note: A four-fold rise in titer between acute and convalescent samples is diagnostic
        O Antigen: Normal <1:80, Positive ≥1:160
        H Antigen: Normal <1:160, Positive ≥1:320
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("WIDAL TEST");
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
