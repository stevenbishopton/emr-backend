package hospital.emr.radiograph.config;

import hospital.emr.radiograph.entities.RadiographCatalog;
import hospital.emr.radiograph.enums.RadiographType;
import hospital.emr.radiograph.repos.RadiographCatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RadiographDataInitializer implements CommandLineRunner {

    private final RadiographCatalogRepository catalogRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRadiographCatalog();
    }

    private void initializeRadiographCatalog() {
        if (catalogRepository.count() == 0) {
            log.info("🏥 Initializing radiograph catalog with default items...");
            
            List<RadiographCatalog> catalogItems = Arrays.asList(
                createCatalogItem("SKULL PA AND AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Skull examination with PA and lateral views"),
                createCatalogItem("PARANASAL SINUSES (PNS)", RadiographType.X_RAY, new BigDecimal("12000.00"), "Paranasal sinuses examination"),
                createCatalogItem("MANDIBLE", RadiographType.X_RAY, new BigDecimal("12000.00"), "Mandible examination"),
                createCatalogItem("CERVICAL SPINE AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Cervical spine with AP and lateral views"),
                createCatalogItem("POSTNATAL SPACE", RadiographType.X_RAY, new BigDecimal("10000.00"), "Postnatal space examination"),
                createCatalogItem("CLAVICLE", RadiographType.X_RAY, new BigDecimal("10000.00"), "Clavicle examination"),
                createCatalogItem("CHEST PA", RadiographType.X_RAY, new BigDecimal("12000.00"), "Chest posteroanterior view"),
                createCatalogItem("CHEST PA/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Chest with PA and lateral views"),
                createCatalogItem("THORACOLUMBER SPINE AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Thoracolumbar spine with AP and lateral views"),
                createCatalogItem("THORACIC", RadiographType.X_RAY, new BigDecimal("12000.00"), "Thoracic spine examination"),
                createCatalogItem("PLAIN ABDOMEN", RadiographType.X_RAY, new BigDecimal("12000.00"), "Plain abdomen examination"),
                createCatalogItem("ABDOMINAL ERECT/SPINE", RadiographType.X_RAY, new BigDecimal("15000.00"), "Abdominal erect and spine views"),
                createCatalogItem("LUMBOSACRAL SPINE AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Lumbosacral spine with AP and lateral views"),
                createCatalogItem("SHOULDER JOINT AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Shoulder joint with AP and lateral views"),
                createCatalogItem("HUMERUS AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Humerus with AP and lateral views"),
                createCatalogItem("ELBOW JOINT AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Elbow joint with AP and lateral views"),
                createCatalogItem("FOREARM AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Forearm with AP and lateral views"),
                createCatalogItem("WRIST AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Wrist with AP and lateral views"),
                createCatalogItem("HAND AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Hand with AP and lateral views"),
                createCatalogItem("PELVIS AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Pelvis with AP and lateral views"),
                createCatalogItem("HIP JOINT AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Hip joint with AP and lateral views"),
                createCatalogItem("FEMUR AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Femur with AP and lateral views"),
                createCatalogItem("KNEE JOINT AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Knee joint with AP and lateral views"),
                createCatalogItem("TIBIA/FIBULA AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Tibia/Fibula with AP and lateral views"),
                createCatalogItem("ANKLE JOINT AP/LAT", RadiographType.X_RAY, new BigDecimal("15000.00"), "Ankle joint with AP and lateral views"),
                createCatalogItem("FOOT AP/LAT/OBLIQUE", RadiographType.X_RAY, new BigDecimal("15000.00"), "Foot with AP, lateral, and oblique views"),
                createCatalogItem("ABDOMINAL ULTRASOUND", RadiographType.ULTRASOUND, new BigDecimal("18000.00"), "Complete abdominal ultrasound examination"),
                createCatalogItem("PELVIC ULTRASOUND", RadiographType.ULTRASOUND, new BigDecimal("15000.00"), "Pelvic ultrasound examination"),
                createCatalogItem("OBSTETRIC ULTRASOUND", RadiographType.ULTRASOUND, new BigDecimal("20000.00"), "Obstetric ultrasound examination"),
                createCatalogItem("TRANSVAGINAL ULTRASOUND", RadiographType.ULTRASOUND, new BigDecimal("18000.00"), "Transvaginal ultrasound examination"),
                createCatalogItem("BREAST ULTRASOUND", RadiographType.ULTRASOUND, new BigDecimal("15000.00"), "Breast ultrasound examination"),
                createCatalogItem("THYROID ULTRASOUND", RadiographType.ULTRASOUND, new BigDecimal("12000.00"), "Thyroid ultrasound examination"),
                createCatalogItem("SCROTAL ULTRASOUND", RadiographType.ULTRASOUND, new BigDecimal("15000.00"), "Scrotal ultrasound examination"),
                createCatalogItem("CAROTID DOPPLER", RadiographType.ULTRASOUND, new BigDecimal("18000.00"), "Carotid doppler ultrasound examination"),
                createCatalogItem("LOWER LIMB DOPPLER", RadiographType.ULTRASOUND, new BigDecimal("20000.00"), "Lower limb doppler ultrasound examination"),
                createCatalogItem("UPPER LIMB DOPPLER", RadiographType.ULTRASOUND, new BigDecimal("18000.00"), "Upper limb doppler ultrasound examination"),
                createCatalogItem("RENAL ULTRASOUND", RadiographType.ULTRASOUND, new BigDecimal("15000.00"), "Renal ultrasound examination"),
                createCatalogItem("PROSTATE ULTRASOUND", RadiographType.ULTRASOUND, new BigDecimal("18000.00"), "Prostate ultrasound examination"),
                createCatalogItem("HEART ECHOCARDIOGRAM", RadiographType.ULTRASOUND, new BigDecimal("25000.00"), "Heart echocardiogram examination")
            );

            catalogRepository.saveAll(catalogItems);
            log.info("✅ Successfully initialized {} radiograph catalog items", catalogItems.size());
        } else {
            log.info("📋 Radiograph catalog already contains {} items", catalogRepository.count());
        }
    }

    private RadiographCatalog createCatalogItem(String name, RadiographType type, BigDecimal price, String description) {
        RadiographCatalog item = new RadiographCatalog();
        item.setName(name);
        item.setType(type);
        item.setPrice(price);
        item.setDescription(description);
        item.setActive(true);
        return item;
    }
}
