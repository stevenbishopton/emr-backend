package hospital.emr.patient.repos;

import hospital.emr.patient.entities.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    MedicalHistory findByPatient_Id(Long patientId);

//    // Find medical history by patient ID
//    Optional<MedicalHistory> findByPatientId(Long patientId);
//
//    // Find medical histories with prescriptions
//    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.prescriptions IS NOT EMPTY")
//    List<MedicalHistory> findMedicalHistoriesWithPrescriptions();
//    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.prescriptions IS NOT EMPTY")
//    Page<MedicalHistory> findMedicalHistoriesWithPrescriptions(Pageable pageable);
//
//    // Find medical histories with vital signs
//    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.vitalSignsList IS NOT EMPTY")
//    List<MedicalHistory> findMedicalHistoriesWithVitalSigns();
//    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.vitalSignsList IS NOT EMPTY")
//    Page<MedicalHistory> findMedicalHistoriesWithVitalSigns(Pageable pageable);
//
//    // Find medical histories with clinical notes
//    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.clinicalNotes IS NOT EMPTY")
//    List<MedicalHistory> findMedicalHistoriesWithClinicalNotes();
//    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.clinicalNotes IS NOT EMPTY")
//    Page<MedicalHistory> findMedicalHistoriesWithClinicalNotes(Pageable pageable);
//
//    // Find medical histories with diagnosis notes
//    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.diagnosisNotes IS NOT EMPTY")
//    List<MedicalHistory> findMedicalHistoriesWithDiagnosisNotes();
//    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.diagnosisNotes IS NOT EMPTY")
//    Page<MedicalHistory> findMedicalHistoriesWithDiagnosisNotes(Pageable pageable);
//
//    // Find medical histories with admissions
//    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.admissions IS NOT EMPTY")
//    List<MedicalHistory> findMedicalHistoriesWithAdmissions();
//    @Query("SELECT mh FROM MedicalHistory mh WHERE mh.admissions IS NOT EMPTY")
//    Page<MedicalHistory> findMedicalHistoriesWithAdmissions(Pageable pageable);
//
//    // Check if medical history exists for patient
//    boolean existsByPatientId(Long patientId);
} 