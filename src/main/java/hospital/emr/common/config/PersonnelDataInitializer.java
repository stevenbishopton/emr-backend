package hospital.emr.common.config;

import hospital.emr.admin.entities.Admin;
import hospital.emr.common.entities.Department;
import hospital.emr.common.entities.Personnel;
import hospital.emr.common.repos.DepartmentRepository;
import hospital.emr.common.repos.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PersonnelDataInitializer implements CommandLineRunner {

    private final PersonnelRepository personnelRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Emergency recovery switch. When set to true (e.g. via
     * {@code -Dspring-boot.run.arguments=--app.security.reset-admin-password=true}
     * or the env var {@code APP_SECURITY_RESET_ADMIN_PASSWORD=true}), the default
     * admin's password is reset to "admin123" on startup. Defaults to false so it
     * has no effect in normal operation.
     */
    @Value("${app.security.reset-admin-password:false}")
    private boolean resetAdminPassword;

    @Override
    public void run(String... args) throws Exception {
        initializeDefaultAdmin();
    }

    private void initializeDefaultAdmin() {
        Optional<Personnel> existing = personnelRepository.findByUsername("admin1");

        if (existing.isEmpty()) {
            log.info("🔐 Creating default admin user...");

            Department department = departmentRepository.findById(1L)
                    .orElseGet(() -> {
                        Department dept = new Department();
                        dept.setName("Administration");
                        return departmentRepository.save(dept);
                    });

            Admin admin = new Admin();
            admin.setNames("System Administrator");
            admin.setUsername("admin1");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setSex(hospital.emr.common.enums.Sex.MALE);
            admin.setPhoneNumber("1234567890");
            admin.setEmail("admin@hospital.com");
            admin.setDepartment(department);

            personnelRepository.save(admin);
            log.info("✅ Default admin user created: username=admin1, password=admin123");
            return;
        }

        if (resetAdminPassword) {
            Personnel admin = existing.get();
            admin.setPassword(passwordEncoder.encode("admin123"));
            personnelRepository.save(admin);
            log.warn("⚠️  Reset admin1 password to the default 'admin123' because "
                    + "app.security.reset-admin-password=true was set. "
                    + "Restart without that flag and change this password.");
        } else {
            log.info("👤 Admin user already exists");
        }
    }
}
