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
 * Testosterone Test
 * Price: 15,000
 * Category: ENDOCRINOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("TESTOSTERONE")
public class TestosteroneTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Adult Male: 300-1000 ng/dL (10.4-34.7 nmol/L)
        Adult Female: 15-70 ng/dL (0.5-2.4 nmol/L)
        Children: Varies by age and pubertal stage
        Note: Levels vary throughout the day (highest in morning)
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("TESTOSTERONE TEST");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("15000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.ENDOCRINOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
