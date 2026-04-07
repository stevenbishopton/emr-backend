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
 * Platelet Count Test
 * Price: 3,000
 * Category: HEMATOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("PLATELET_COUNT")
public class PlateletCountTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Adult: 150,000-450,000 /μL (150-450 x10^9/L)
        Children: 150,000-450,000 /μL
        Newborn: 150,000-300,000 /μL
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("PLATELET COUNT");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("3000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.HEMATOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
