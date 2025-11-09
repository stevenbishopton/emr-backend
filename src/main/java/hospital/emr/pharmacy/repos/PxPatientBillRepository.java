package hospital.emr.pharmacy.repos;

import hospital.emr.pharmacy.entities.PxPatientBill;
import hospital.emr.pharmacy.enums.PxPatientBillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PxPatientBillRepository extends JpaRepository<PxPatientBill, Long> {
    List<PxPatientBill> findAllByStatus(PxPatientBillStatus status);

    Optional<PxPatientBill> findByPatientIdAndStatus(Long patientId, PxPatientBillStatus pxPatientBillStatus);
}
