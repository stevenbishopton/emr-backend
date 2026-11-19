package hospital.emr.patient.repos;

import hospital.emr.patient.entities.MedicationAdministration;
import hospital.emr.patient.entities.PrescriptionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicationAdministrationRepository extends JpaRepository<MedicationAdministration, Long> {

    List<MedicationAdministration> findByPrescriptionEntry(PrescriptionEntry prescriptionEntry);

    Optional<MedicationAdministration> findByPrescriptionEntryAndAdministrationDateAndAdministrationTime(
            PrescriptionEntry prescriptionEntry, LocalDate administrationDate, LocalTime administrationTime);

    List<MedicationAdministration> findByPrescriptionEntry_Prescription_MedicalHistory_IdOrderByAdministrationDateDesc(
            Long medicalHistoryId);

    void deleteByPrescriptionEntry(PrescriptionEntry prescriptionEntry);
}
