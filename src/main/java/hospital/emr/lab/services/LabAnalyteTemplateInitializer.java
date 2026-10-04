package hospital.emr.lab.services;

import hospital.emr.lab.entities.LabAnalyteTemplate;
import hospital.emr.lab.entities.LabTest;
import hospital.emr.lab.repos.LabAnalyteTemplateRepository;
import hospital.emr.lab.repos.LabTestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Seeds default analyte templates (analytes, units, reference and critical ranges)
 * for common panels. Ranges mirror the reference-range text already defined on the
 * test entities. Labs should review and adjust these to local policy.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LabAnalyteTemplateInitializer {

    private final LabAnalyteTemplateRepository templateRepository;
    private final LabTestRepository testRepository;

    @Bean
    @Order(2)
    @Transactional
    public CommandLineRunner initializeLabAnalyteTemplates() {
        return args -> {
            if (templateRepository.count() > 0) {
                log.info("Lab analyte templates already initialized. Skipping...");
                return;
            }
            int created = 0;
            created += seedHaemogram();
            created += seedLft();
            created += seedEuCcr();
            created += seedLipid("FASTING SERUM LIPID PROFILE (FLP)");
            created += seedLipid("LIPID PROFILE");
            created += seedThyroid();
            created += seedFbs();
            log.info("Lab analyte template initialization completed. Created: {}", created);
        };
    }

    private int seedHaemogram() {
        Long id = testId("COMPLETE HAEMOGRAM (FBC)");
        if (id == null) return 0;
        save(id, "Hemoglobin", "g/dL", d("13.5"), d("17.5"), d("7.0"), d("20.0"), 0);
        save(id, "PCV", "%", d("40"), d("54"), d("20"), d("60"), 1);
        save(id, "WBC", "x10^9/L", d("4.0"), d("11.0"), d("2.0"), d("30.0"), 2);
        save(id, "Platelets", "x10^9/L", d("150"), d("450"), d("50"), d("1000"), 3);
        save(id, "RBC", "x10^12/L", d("4.5"), d("6.0"), null, null, 4);
        save(id, "MCV", "fL", d("80"), d("100"), null, null, 5);
        save(id, "MCH", "pg", d("27"), d("31"), null, null, 6);
        save(id, "MCHC", "g/dL", d("32"), d("36"), null, null, 7);
        return 8;
    }

    private int seedLft() {
        Long id = testId("LIVER FUNCTION TEST (LFT)");
        if (id == null) return 0;
        save(id, "ALT", "U/L", d("7"), d("56"), null, null, 0);
        save(id, "AST", "U/L", d("10"), d("40"), null, null, 1);
        save(id, "ALP", "U/L", d("44"), d("147"), null, null, 2);
        save(id, "Total Bilirubin", "mg/dL", d("0.1"), d("1.2"), null, d("15"), 3);
        save(id, "Direct Bilirubin", "mg/dL", d("0"), d("0.3"), null, null, 4);
        save(id, "Total Protein", "g/dL", d("6.0"), d("8.3"), null, null, 5);
        save(id, "Albumin", "g/dL", d("3.5"), d("5.0"), null, null, 6);
        return 7;
    }

    private int seedEuCcr() {
        Long id = testId("ELECTROLYTE, UREA, CREATININE (E/U/Cr)");
        if (id == null) return 0;
        save(id, "Sodium", "mEq/L", d("136"), d("145"), d("120"), d("160"), 0);
        save(id, "Potassium", "mEq/L", d("3.5"), d("5.0"), d("2.5"), d("6.5"), 1);
        save(id, "Chloride", "mEq/L", d("98"), d("107"), null, null, 2);
        save(id, "Bicarbonate", "mEq/L", d("22"), d("28"), null, null, 3);
        save(id, "Urea", "mg/dL", d("7"), d("20"), null, null, 4);
        save(id, "Creatinine", "mg/dL", d("0.7"), d("1.3"), null, null, 5);
        return 6;
    }

    private int seedLipid(String testName) {
        Long id = testId(testName);
        if (id == null) return 0;
        save(id, "Total Cholesterol", "mg/dL", null, d("200"), null, null, 0);
        save(id, "LDL Cholesterol", "mg/dL", null, d("100"), null, null, 1);
        save(id, "HDL Cholesterol", "mg/dL", d("40"), null, null, null, 2);
        save(id, "Triglycerides", "mg/dL", null, d("150"), null, null, 3);
        return 4;
    }

    private int seedThyroid() {
        Long id = testId("THYROID FUNCTION TEST");
        if (id == null) return 0;
        save(id, "TSH", "mIU/L", d("0.4"), d("4.0"), null, null, 0);
        save(id, "Free T4", "ng/dL", d("0.8"), d("1.8"), null, null, 1);
        save(id, "Free T3", "pg/mL", d("2.3"), d("4.2"), null, null, 2);
        save(id, "Total T4", "µg/dL", d("5.0"), d("12.0"), null, null, 3);
        save(id, "Total T3", "ng/dL", d("80"), d("200"), null, null, 4);
        return 5;
    }

    private int seedFbs() {
        Long id = testId("FASTING BLOOD SUGAR (FBS)");
        if (id == null) return 0;
        save(id, "Fasting Glucose", "mg/dL", d("70"), d("100"), d("40"), d("400"), 0);
        return 1;
    }

    private Long testId(String name) {
        return testRepository.findByNameIgnoreCase(name)
                .map(LabTest::getId)
                .orElse(null);
    }

    private void save(Long testId, String analyte, String unit, BigDecimal low, BigDecimal high,
                      BigDecimal critLow, BigDecimal critHigh, int order) {
        templateRepository.save(new LabAnalyteTemplate(testId, analyte, unit, low, high, critLow, critHigh, order));
    }

    private BigDecimal d(String value) {
        return new BigDecimal(value);
    }
}
