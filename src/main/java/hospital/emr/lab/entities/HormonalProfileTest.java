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
 * Hormonal Profile Test
 * Price: 48,000
 * Category: ENDOCRINOLOGY
 * Sample Type: BLOOD
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("HORMONAL_PROFILE")
public class HormonalProfileTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        FSH (Follicle Stimulating Hormone): Male 1.5-12.4 mIU/mL, Female varies by cycle
        LH (Luteinizing Hormone): Male 1.7-8.6 mIU/mL, Female varies by cycle
        Prolactin: Male 2-18 ng/mL, Female 3-30 ng/mL
        Estradiol: Male 7.6-42.6 pg/mL, Female varies by cycle
        Progesterone: Male <1 ng/mL, Female varies by cycle
        Testosterone: Male 300-1000 ng/dL, Female 15-70 ng/dL
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("HORMONAL PROFILE");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("48000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.ENDOCRINOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.BLOOD);
        }
    }
}
