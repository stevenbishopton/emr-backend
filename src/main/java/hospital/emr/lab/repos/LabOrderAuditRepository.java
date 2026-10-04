package hospital.emr.lab.repos;

import hospital.emr.lab.entities.LabOrderAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabOrderAuditRepository extends JpaRepository<LabOrderAudit, Long> {

    List<LabOrderAudit> findByOrderIdOrderByTimestampAsc(Long orderId);
}
