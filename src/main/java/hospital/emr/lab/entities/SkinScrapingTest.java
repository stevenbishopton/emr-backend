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
 * Skin Scraping Test
 * Price: 7,000
 * Category: MICROBIOLOGY
 * Sample Type: SKIN
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("SKIN_SCRAPING")
public class SkinScrapingTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange = """
        Normal: No fungi, parasites, or pathogens detected
        Common Findings: Dermatophytes (ringworm), Scabies mites, Candida
        Microscopy: Direct examination for fungi and parasites
        Culture: May be performed for fungal identification
        """;

    @PrePersist
    @PreUpdate
    public void initializeDefaults() {
        if (super.getName() == null) {
            super.setName("SKIN SCRAPPING");
        }
        if (super.getPrice() == null) {
            super.setPrice(new BigDecimal("7000.00"));
        }
        if (super.getCategory() == null) {
            super.setCategory(TestCategory.MICROBIOLOGY);
        }
        if (super.getSampleType() == null) {
            super.setSampleType(SampleType.SKIN);
        }
    }
}
