package hospital.emr.common.repos;

import hospital.emr.common.entities.Personnel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonnelRepository extends JpaRepository< Personnel, Long> {
}
