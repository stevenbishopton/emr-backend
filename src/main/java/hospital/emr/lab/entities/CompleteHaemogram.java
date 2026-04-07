package hospital.emr.lab.entities;

import hospital.emr.lab.enums.SampleType;
import hospital.emr.lab.enums.TestCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Complete Haemogram (Full Blood Count - FBC) Test
 * Price: 4,000
 * Category: HEMATOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("COMPLETE_HAEMOGRAM")
public class CompleteHaemogram extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Hemoglobin: Adult Male 13.5-17.5 g/dL, Adult Female 12.0-15.5 g/dL, Children 11.0-16.0 g/dL
        PCV (Hematocrit): Adult Male 40-54%, Adult Female 36-48%, Children 30-42%
        WBC (White Blood Cells): 4.0-11.0 x10^9/L
        Platelets: 150-450 x10^9/L
        RBC (Red Blood Cells): Adult Male 4.5-6.0 x10^12/L, Adult Female 4.0-5.5 x10^12/L, Children 3.8-5.5 x10^12/L
        MCV: 80-100 fL
        MCH: 27-31 pg
        MCHC: 32-36 g/dL
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("COMPLETE HAEMOGRAM (FBC)");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("4000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.HEMATOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
