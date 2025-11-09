package hospital.emr.patient.repos;

import hospital.emr.patient.entities.VitalSigns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VitalSignsRepository extends JpaRepository<VitalSigns, Long> {
    List<VitalSigns> findByMedicalHistory_Id(Long medicalHistoryId);
    List<VitalSigns> findByVisit_Id(Long visitId);

    // Get latest vital signs by medical history
    Optional<VitalSigns> findTopByMedicalHistory_IdOrderByTimeTakenDesc(Long medicalHistoryId);

    // Get latest vital signs by visit
    Optional<VitalSigns> findTopByVisit_IdOrderByTimeTakenDesc(Long visitId);
}