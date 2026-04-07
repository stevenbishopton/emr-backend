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
 * Stool Microscopy Test
 * Price: 5,000
 * Category: STOOL_ANALYSIS
 * Sample Type: STOOL
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("STOOL_MICROSCOPY")
public class StoolMicroscopyTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: No ova, cysts, or parasites detected
        Common Parasites: Entamoeba histolytica, Giardia lamblia, Ascaris lumbricoides
        White Blood Cells: None (Presence indicates inflammation)
        Red Blood Cells: None
        Note: Multiple samples may be required for accurate detection
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("STOOL MICROSCOPY");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("5000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.STOOL_ANALYSIS);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.STOOL);
        }
    }
}
