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
 * Hemoglobin (HB) Test
 * Price: 2,000
 * Category: HEMATOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("HEMOGLOBIN")
public class HemoglobinTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Adult Male: 13.5-17.5 g/dL
        Adult Female: 12.0-15.5 g/dL
        Children: 11.0-16.0 g/dL
        Newborn: 14.0-24.0 g/dL
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("HAEMOGLOBIN (HB)");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("2000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.HEMATOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
