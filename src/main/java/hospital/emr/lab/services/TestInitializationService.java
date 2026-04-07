package hospital.emr.lab.services;

import hospital.emr.lab.entities.*;
import hospital.emr.lab.repos.LabTestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TestInitializationService {

    private final LabTestRepository labTestRepository;

    @Bean
    @Transactional
    public CommandLineRunner initializeLabTests() {
        return args -> {
            if (labTestRepository.count() > 0) {
                log.info("Lab tests already initialized. Skipping...");
                return;
            }

            log.info("Starting lab test initialization...");

            List<LabTest> allTests = createAllTests();

            int created = 0;
            int skipped = 0;

            for (LabTest test : allTests) {
                if (!labTestRepository.existsByNameIgnoreCase(test.getName())) {
                    initializeTestDefaults(test);
                    labTestRepository.save(test);
                    created++;
                    log.debug("Created test: {}", test.getName());
                } else {
                    skipped++;
                    log.debug("Test already exists, skipping: {}", test.getName());
                }
            }

            log.info("Lab test initialization completed. Created: {}, Skipped: {}, Total: {}",
                    created, skipped, allTests.size());
        };
    }

    private List<LabTest> createAllTests() {
        return List.of(
                // HEMATOLOGY
                new CompleteHaemogram(),
                new HemoglobinTest(),
                new PCVTest(),
                new ESRTest(),
                new ClottingTimeTest(),
                new PlateletCountTest(),

                // INFECTIOUS_DISEASES
                new MPTest(),
                new WidalTest(),
                new HIVScreeningTest(),
                new HIVConfirmationTest(),
                new HPyloriTest(),
                new VDRLTest(),
                new HepatitisBCTest(),
                new ChlamydiaTest(),

                // BLOOD_BANKING
                new HBGenotypeTest(),
                new BloodGroupTest(),
                new BloodScreeningRHPositiveTest(),
                new BloodScreeningRHNegativeTest(),

                // BIOCHEMISTRY
                new FBSTest(),
                new LFTTest(),
                new EUCCrTest(),
                new FLPTest(),
                new CholesterolTest(),
                new BilirubinTest(),
                new RheumatoidFactorTest(),
                new CRPTest(),
                new UricAcidTest(),
                new LipidProfileTest(),
                new ElectrolytesTest(),

                // URINALYSIS
                new UrinalysisTest(),
                new UrineMicroscopyTest(),
                new UrineMCSTest(),

                // MICROBIOLOGY
                new StoolMCSTest(),
                new HVSTest(),
                new UrethralSwabTest(),
                new SemenMCSTest(),
                new WoundSwabTest(),
                new SputumMCSTest(),
                new BloodCultureTest(),
                new SkinScrapingTest(),

                // ENDOCRINOLOGY
                new TestosteroneTest(),
                new ThyroidFunctionTest(),
                new PSATest(),
                new HormonalProfileTest(),
                new AMHTest(),

                // PARASITOLOGY
                new BloodMicrofilariaTest(),

                // STOOL_ANALYSIS
                new StoolMicroscopyTest(),
                new OccultBloodTest(),

                // REPRODUCTIVE_HEALTH
                new SemenAnalysisTest(),
                new PregnancyTest(),
                new OvulationTest()
        );
    }

    private void initializeTestDefaults(LabTest test) {
        if (test instanceof CompleteHaemogram completeHaemogram) {
            completeHaemogram.initializeDefaults();
        } else if (test instanceof HemoglobinTest hemoglobinTest) {
            hemoglobinTest.initializeDefaults();
        } else if (test instanceof PCVTest pcvTest) {
            pcvTest.initializeDefaults();
        } else if (test instanceof MPTest mpTest) {
            mpTest.initializeDefaults();
        } else if (test instanceof WidalTest widalTest) {
            widalTest.initializeDefaults();
        } else if (test instanceof ESRTest esrTest) {
            esrTest.initializeDefaults();
        } else if (test instanceof ClottingTimeTest clottingTimeTest) {
            clottingTimeTest.initializeDefaults();
        } else if (test instanceof PlateletCountTest plateletCountTest) {
            plateletCountTest.initializeDefaults();
        } else if (test instanceof BloodMicrofilariaTest bloodMicrofilariaTest) {
            bloodMicrofilariaTest.initializeDefaults();
        } else if (test instanceof HIVScreeningTest hivScreeningTest) {
            hivScreeningTest.initializeDefaults();
        } else if (test instanceof HIVConfirmationTest hivConfirmationTest) {
            hivConfirmationTest.initializeDefaults();
        } else if (test instanceof HPyloriTest hPyloriTest) {
            hPyloriTest.initializeDefaults();
        } else if (test instanceof VDRLTest vdrlTest) {
            vdrlTest.initializeDefaults();
        } else if (test instanceof HepatitisBCTest hepatitisBCTest) {
            hepatitisBCTest.initializeDefaults();
        } else if (test instanceof HBGenotypeTest hbGenotypeTest) {
            hbGenotypeTest.initializeDefaults();
        } else if (test instanceof BloodGroupTest bloodGroupTest) {
            bloodGroupTest.initializeDefaults();
        } else if (test instanceof BloodScreeningRHPositiveTest bloodScreeningRHPositiveTest) {
            bloodScreeningRHPositiveTest.initializeDefaults();
        } else if (test instanceof BloodScreeningRHNegativeTest bloodScreeningRHNegativeTest) {
            bloodScreeningRHNegativeTest.initializeDefaults();
        } else if (test instanceof FBSTest fbsTest) {
            fbsTest.initializeDefaults();
        } else if (test instanceof LFTTest lftTest) {
            lftTest.initializeDefaults();
        } else if (test instanceof EUCCrTest euccrTest) {
            euccrTest.initializeDefaults();
        } else if (test instanceof FLPTest flpTest) {
            flpTest.initializeDefaults();
        } else if (test instanceof CholesterolTest cholesterolTest) {
            cholesterolTest.initializeDefaults();
        } else if (test instanceof BilirubinTest bilirubinTest) {
            bilirubinTest.initializeDefaults();
        } else if (test instanceof RheumatoidFactorTest rheumatoidFactorTest) {
            rheumatoidFactorTest.initializeDefaults();
        } else if (test instanceof CRPTest crpTest) {
            crpTest.initializeDefaults();
        } else if (test instanceof UricAcidTest uricAcidTest) {
            uricAcidTest.initializeDefaults();
        } else if (test instanceof LipidProfileTest lipidProfileTest) {
            lipidProfileTest.initializeDefaults();
        } else if (test instanceof ElectrolytesTest electrolytesTest) {
            electrolytesTest.initializeDefaults();
        } else if (test instanceof TestosteroneTest testosteroneTest) {
            testosteroneTest.initializeDefaults();
        } else if (test instanceof UrinalysisTest urinalysisTest) {
            urinalysisTest.initializeDefaults();
        } else if (test instanceof UrineMicroscopyTest urineMicroscopyTest) {
            urineMicroscopyTest.initializeDefaults();
        } else if (test instanceof UrineMCSTest urineMCSTest) {
            urineMCSTest.initializeDefaults();
        } else if (test instanceof StoolMicroscopyTest stoolMicroscopyTest) {
            stoolMicroscopyTest.initializeDefaults();
        } else if (test instanceof StoolMCSTest stoolMCSTest) {
            stoolMCSTest.initializeDefaults();
        } else if (test instanceof HVSTest hvsTest) {
            hvsTest.initializeDefaults();
        } else if (test instanceof UrethralSwabTest urethralSwabTest) {
            urethralSwabTest.initializeDefaults();
        } else if (test instanceof SemenAnalysisTest semenAnalysisTest) {
            semenAnalysisTest.initializeDefaults();
        } else if (test instanceof SemenMCSTest semenMCSTest) {
            semenMCSTest.initializeDefaults();
        } else if (test instanceof WoundSwabTest woundSwabTest) {
            woundSwabTest.initializeDefaults();
        } else if (test instanceof SputumMCSTest sputumMCSTest) {
            sputumMCSTest.initializeDefaults();
        } else if (test instanceof BloodCultureTest bloodCultureTest) {
            bloodCultureTest.initializeDefaults();
        } else if (test instanceof ThyroidFunctionTest thyroidFunctionTest) {
            thyroidFunctionTest.initializeDefaults();
        } else if (test instanceof PSATest psaTest) {
            psaTest.initializeDefaults();
        } else if (test instanceof HormonalProfileTest hormonalProfileTest) {
            hormonalProfileTest.initializeDefaults();
        } else if (test instanceof AMHTest amhTest) {
            amhTest.initializeDefaults();
        } else if (test instanceof PregnancyTest pregnancyTest) {
            pregnancyTest.initializeDefaults();
        } else if (test instanceof SkinScrapingTest skinScrapingTest) {
            skinScrapingTest.initializeDefaults();
        } else if (test instanceof OccultBloodTest occultBloodTest) {
            occultBloodTest.initializeDefaults();
        } else if (test instanceof OvulationTest ovulationTest) {
            ovulationTest.initializeDefaults();
        } else if (test instanceof ChlamydiaTest chlamydiaTest) {
            chlamydiaTest.initializeDefaults();
        }
    }
}