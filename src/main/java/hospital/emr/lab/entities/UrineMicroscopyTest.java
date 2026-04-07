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
 * Urine Microscopy Test
 * Price: 2,000
 * Category: URINALYSIS
 * Sample Type: URINE
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("URINE_MICROSCOPY")
public class UrineMicroscopyTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Red Blood Cells: 0-3 per HPF
        White Blood Cells: 0-5 per HPF
        Epithelial Cells: Few
        Casts: 0-2 hyaline casts per LPF
        Crystals: Occasional
        Bacteria: None
        Yeast: None
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("URINE MICROSCOPY");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("2000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.URINALYSIS);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.URINE);
        }
    }
}
