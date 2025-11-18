package hospital.emr.patient.repos;

import hospital.emr.patient.entities.Admission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Long> {

    // Historical admissions
    List<Admission> findByWard_Id(Long wardId);
    List<Admission> findByMedicalHistory_Patient_IdOrderByAdmissionDateDesc(Long patientId);

    // Active admissions
    List<Admission> findByDischargeDateIsNull();
    List<Admission> findByWard_IdAndDischargeDateIsNull(Long wardId);
    List<Admission> findByMedicalHistory_Patient_IdAndDischargeDateIsNull(Long patientId);
    Optional<Admission> findTopByMedicalHistory_Patient_IdAndDischargeDateIsNullOrderByAdmissionDateDesc(Long patientId);
    boolean existsByMedicalHistory_Patient_IdAndDischargeDateIsNull(Long patientId);

    // Date range queries
    List<Admission> findByAdmissionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Admission> findByAdmissionDateAfter(LocalDateTime date);
    List<Admission> findByAdmissionDateBefore(LocalDateTime date);

    Optional<Admission> findByVisit_Id(Long visitId);
}