package hospital.emr.patient.repos;

import hospital.emr.patient.entities.PrescriptionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionEntryRepository extends JpaRepository<PrescriptionEntry, Long> {
//
//    // Find prescription entries by prescription ID
//    List<PrescriptionEntry> findByPrescriptionId(Long prescriptionId);
//    Page<PrescriptionEntry> findByPrescriptionId(Long prescriptionId, Pageable pageable);
//
//    // Find prescription entries by item name
//    @Query("SELECT pe FROM PrescriptionEntry pe WHERE pe.itemName LIKE %:itemName%")
//    List<PrescriptionEntry> findByItemNameContaining(@Param("itemName") String itemName);
//    @Query("SELECT pe FROM PrescriptionEntry pe WHERE pe.itemName LIKE %:itemName%")
//    Page<PrescriptionEntry> findByItemNameContaining(@Param("itemName") String itemName, Pageable pageable);
//
//    // Find prescription entries by dosage
//    List<PrescriptionEntry> findByDosage(String dosage);
//    Page<PrescriptionEntry> findByDosage(String dosage, Pageable pageable);
//
//    // Find prescription entries by frequency
//    List<PrescriptionEntry> findByFrequency(String frequency);
//    Page<PrescriptionEntry> findByFrequency(String frequency, Pageable pageable);
//
//    // Find prescription entries by duration
//    List<PrescriptionEntry> findByDuration(String duration);
//    Page<PrescriptionEntry> findByDuration(String duration, Pageable pageable);
//
//    // Find prescription entries with specific instructions
//    @Query("SELECT pe FROM PrescriptionEntry pe WHERE pe.instructions LIKE %:instructions%")
//    List<PrescriptionEntry> findByInstructionsContaining(@Param("instructions") String instructions);
//    @Query("SELECT pe FROM PrescriptionEntry pe WHERE pe.instructions LIKE %:instructions%")
//    Page<PrescriptionEntry> findByInstructionsContaining(@Param("instructions") String instructions, Pageable pageable);
//
//    // Find prescription entries by prescription and item name
//    @Query("SELECT pe FROM PrescriptionEntry pe WHERE pe.prescription.id = :prescriptionId AND pe.itemName LIKE %:itemName%")
//    List<PrescriptionEntry> findByPrescriptionIdAndItemNameContaining(
//            @Param("prescriptionId") Long prescriptionId,
//            @Param("itemName") String itemName
//    );
//    @Query("SELECT pe FROM PrescriptionEntry pe WHERE pe.prescription.id = :prescriptionId AND pe.itemName LIKE %:itemName%")
//    Page<PrescriptionEntry> findByPrescriptionIdAndItemNameContaining(
//            @Param("prescriptionId") Long prescriptionId,
//            @Param("itemName") String itemName,
//            Pageable pageable
//    );
//
//    // Count prescription entries by prescription
//    long countByPrescriptionId(Long prescriptionId);
} 