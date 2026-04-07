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
 * Packed Cell Volume (PCV) Test
 * Price: 2,000
 * Category: HEMATOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("PCV")
public class PCVTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Adult Male: 40-54%
        Adult Female: 36-48%
        Children: 30-42%
        Newborn: 44-64%
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("PACKED CELL VOLUME (PCV)");
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
