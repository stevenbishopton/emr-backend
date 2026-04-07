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
 * HIV 1&2 Screening Test
 * Price: 5,000
 * Category: INFECTIOUS_DISEASES
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("HIV_SCREENING")
public class HIVScreeningTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: Non-reactive (Negative)
        Positive: Reactive (Requires confirmation)
        Window Period: 2-4 weeks after exposure
        Note: All positive results must be confirmed with HIV 1&2 Confirmation test
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("HIV 1&2 SCREENING");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("5000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.INFECTIOUS_DISEASES);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
