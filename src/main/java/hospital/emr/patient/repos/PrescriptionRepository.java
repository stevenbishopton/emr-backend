package hospital.emr.patient.repos;

import hospital.emr.patient.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findAllByMedicalHistoryId(Long id);

    Optional<Prescription> findByVisit_Id(Long visitId);

    Optional<Prescription> findTopByMedicalHistory_Patient_IdOrderByCreatedAtDesc(Long patientId);
//
//    // Find prescriptions by medical history ID
//    List<Prescription> findByMedicalHistoryId(Long medicalHistoryId);
//    Page<Prescription> findByMedicalHistoryId(Long medicalHistoryId, Pageable pageable);
//
//    // Find prescriptions by prescriber (doctor) ID
//    List<Prescription> findByPrescriberId(Long prescriberId);
//    Page<Prescription> findByPrescriberId(Long prescriberId, Pageable pageable);
//
//    // Find prescriptions created between dates
//    List<Prescription> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
//    Page<Prescription> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
//
//    // Find prescriptions updated between dates
//    List<Prescription> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
//    Page<Prescription> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
//
//    // Find prescriptions by prescriber and date range
//    @Query("SELECT p FROM Prescription p WHERE p.prescriber.id = :prescriberId AND p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt DESC")
//    List<Prescription> findByPrescriberIdAndCreatedAtBetween(
//            @Param("prescriberId") Long prescriberId,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate
//    );
//    @Query("SELECT p FROM Prescription p WHERE p.prescriber.id = :prescriberId AND p.createdAt BETWEEN :startDate AND :endDate ORDER BY p.createdAt DESC")
//    Page<Prescription> findByPrescriberIdAndCreatedAtBetween(
//            @Param("prescriberId") Long prescriberId,
//            @Param("startDate") LocalDateTime startDate,
//            @Param("endDate") LocalDateTime endDate,
//            Pageable pageable
//    );
//
//    // Find latest prescriptions for a medical history
//    @Query("SELECT p FROM Prescription p WHERE p.medicalHistory.id = :medicalHistoryId ORDER BY p.createdAt DESC")
//    List<Prescription> findLatestPrescriptionsByMedicalHistoryId(@Param("medicalHistoryId") Long medicalHistoryId);
//    @Query("SELECT p FROM Prescription p WHERE p.medicalHistory.id = :medicalHistoryId ORDER BY p.createdAt DESC")
//    Page<Prescription> findLatestPrescriptionsByMedicalHistoryId(@Param("medicalHistoryId") Long medicalHistoryId, Pageable pageable);
//
//    // Find prescriptions with entries
//    @Query("SELECT p FROM Prescription p WHERE p.prescriptionEntries IS NOT EMPTY")
//    List<Prescription> findPrescriptionsWithEntries();
//    @Query("SELECT p FROM Prescription p WHERE p.prescriptionEntries IS NOT EMPTY")
//    Page<Prescription> findPrescriptionsWithEntries(Pageable pageable);
//
//    // Find prescriptions without entries
//    @Query("SELECT p FROM Prescription p WHERE p.prescriptionEntries IS EMPTY")
//    List<Prescription> findPrescriptionsWithoutEntries();
//    @Query("SELECT p FROM Prescription p WHERE p.prescriptionEntries IS EMPTY")
//    Page<Prescription> findPrescriptionsWithoutEntries(Pageable pageable);
//
//    // Count prescriptions by prescriber
//    long countByPrescriberId(Long prescriberId);
//
//    // Count prescriptions by medical history
//    long countByMedicalHistoryId(Long medicalHistoryId);
} 