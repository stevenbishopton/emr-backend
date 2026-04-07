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
 * Blood Microfilaria Test
 * Price: 3,000
 * Category: PARASITOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("BLOOD_MICROFILARIA")
public class BloodMicrofilariaTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: Negative (No microfilariae detected)
        Positive: Microfilariae detected
        Species: Wuchereria bancrofti, Brugia malayi, Loa loa
        Note: Best collected between 10 PM - 2 AM for nocturnal species
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("BLOOD MICROFILARIA");
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
