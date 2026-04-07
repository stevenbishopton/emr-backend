package hospital.emr.bill.repos;

import hospital.emr.bill.entities.PatientDeptBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientDeptBillRepository extends JpaRepository<PatientDeptBill, Long> {

    List<PatientDeptBill> findByPatientId(String patientId);

    List<PatientDeptBill> findByPatientNamesContainingIgnoreCase(String patientNames);

    List<PatientDeptBill> findByIssuer(String issuer);

    List<PatientDeptBill> findByIsAdmittedTrue();

    List<PatientDeptBill> findByIsPaidTrue();

    List<PatientDeptBill> findByVisitId(Long visitId);

    Optional<PatientDeptBill> findByIdAndPatientId(Long id, String patientId);

    List<PatientDeptBill> findByVisitIdAndIsAdmittedTrue(Long visitId);
}