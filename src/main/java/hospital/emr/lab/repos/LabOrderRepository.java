package hospital.emr.lab.repos;

import hospital.emr.lab.entities.LabOrder;
import hospital.emr.lab.enums.LabOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabOrderRepository extends JpaRepository<LabOrder, Long>, JpaSpecificationExecutor<LabOrder> {

    Page<LabOrder> findByStatus(LabOrderStatus status, Pageable pageable);

    Page<LabOrder> findByPatientId(Long patientId, Pageable pageable);

    List<LabOrder> findByPatientIdOrderByCreatedAtDesc(Long patientId);

    Page<LabOrder> findByStatusIn(List<LabOrderStatus> statuses, Pageable pageable);

    long countByStatus(LabOrderStatus status);
}
