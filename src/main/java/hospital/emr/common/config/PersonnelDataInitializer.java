package hospital.emr.common.config;

import hospital.emr.admin.entities.Admin;
import hospital.emr.common.entities.Department;
import hospital.emr.common.repos.DepartmentRepository;
import hospital.emr.common.repos.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PersonnelDataInitializer implements CommandLineRunner {

    private final PersonnelRepository personnelRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeDefaultAdmin();
    }

    private void initializeDefaultAdmin() {
        if (personnelRepository.findByUsername("admin1").isEmpty()) {
            log.info("🔐 Creating default admin user...");
            
            // Get or create default department
            Department department = departmentRepository.findById(1L)
                    .orElseGet(() -> {
                        Department dept = new Department();
                        dept.setName("Administration");
                        return departmentRepository.save(dept);
                    });

            // Create admin user
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
        } else {
            log.info("👤 Admin user already exists");
        }
    }
}
