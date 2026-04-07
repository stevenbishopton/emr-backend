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
 * Erythrocyte Sedimentation Rate (ESR) Test
 * Price: 3,000
 * Category: HEMATOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("ESR")
public class ESRTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Adult Male: 0-15 mm/hr
        Adult Female: 0-20 mm/hr
        Children: 0-10 mm/hr
        Elderly (>50 years): Male 0-20 mm/hr, Female 0-30 mm/hr
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("ERYTHROCYTE SEDIMENTATION RATE (ESR)");
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
