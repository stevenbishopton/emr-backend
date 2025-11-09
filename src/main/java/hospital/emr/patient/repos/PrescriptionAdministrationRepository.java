package hospital.emr.patient.repos;

import hospital.emr.patient.entities.AdmissionPrescriptionEntry;
import hospital.emr.patient.entities.PrescriptionAdministration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionAdministrationRepository extends JpaRepository<PrescriptionAdministration, Long> {

    Optional<PrescriptionAdministration> findByPrescriptionEntryAndAdministrationDateAndAdministrationTime(
            AdmissionPrescriptionEntry prescriptionEntry,
            LocalDate administrationDate,
            LocalTime administrationTime);

    List<PrescriptionAdministration> findByPrescriptionEntry(AdmissionPrescriptionEntry prescriptionEntry);

    List<PrescriptionAdministration> findByAdministrationDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT pa FROM PrescriptionAdministration pa WHERE pa.prescriptionEntry.chart.admission.id = :admissionId")
    List<PrescriptionAdministration> findByAdmissionId(@Param("admissionId") Long admissionId);
}