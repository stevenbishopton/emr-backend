package hospital.emr.reception.repos;

import hospital.emr.reception.entities.Visit;
import hospital.emr.reception.enums.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByPatientId(Long patientId);
    List<Visit> findByStatus(VisitStatus status);
}
