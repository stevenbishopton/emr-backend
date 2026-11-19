package hospital.emr;

import hospital.emr.common.enums.Sex;
import hospital.emr.patient.dtos.MedicationAdministrationDTO;
import hospital.emr.patient.dtos.PrescriptionEntryDTO;
import hospital.emr.patient.entities.Admission;
import hospital.emr.patient.entities.MedicalHistory;
import hospital.emr.patient.entities.Patient;
import hospital.emr.patient.repos.AdmissionRepository;
import hospital.emr.patient.repos.MedicalHistoryRepository;
import hospital.emr.patient.repos.PatientRepository;
import hospital.emr.patient.services.MedicationAdministrationService;
import hospital.emr.patient.services.PrescriptionService;
import hospital.emr.reception.entities.Visit;
import hospital.emr.reception.enums.VisitStatus;
import hospital.emr.reception.repos.VisitRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies the unified medication model: a nurse records a dose against exactly
 * the medication the doctor prescribed, and that dose persists and is returned.
 */
@SpringBootTest
@ActiveProfiles("h2test")
@Transactional
class PrescriptionAdministrationFlowTest {

    @Autowired private PatientRepository patientRepository;
    @Autowired private MedicalHistoryRepository medicalHistoryRepository;
    @Autowired private VisitRepository visitRepository;
    @Autowired private AdmissionRepository admissionRepository;
    @Autowired private PrescriptionService prescriptionService;
    @Autowired private MedicationAdministrationService administrationService;
    @Autowired private EntityManager em;

    @Test
    void doseIsRecordedAgainstPrescribedMedicationAndReturned() {
        long unique = System.nanoTime();

        Patient patient = patientRepository.save(Patient.builder()
                .code("TEST-" + unique)
                .names("Test Patient")
                .sex(Sex.MALE)
                .phoneNumber(String.valueOf(unique).substring(0, 10))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .build());

        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setPatient(patient);
        medicalHistory = medicalHistoryRepository.save(medicalHistory);

        Visit visit = new Visit();
        visit.setPatient(patient);
        visit.setVisitDateTime(LocalDateTime.now());
        visit.setStatus(VisitStatus.IN_QUEUE);
        visit = visitRepository.save(visit);

        Admission admission = new Admission();
        admission.setMedicalHistory(medicalHistory);
        admission.setVisit(visit);
        admission.setAdmissionDate(LocalDateTime.now());
        admission = admissionRepository.save(admission);
        assertNotNull(admission.getId());

        // Doctor (or MAR) prescribes a medication -> a PrescriptionEntry.
        PrescriptionEntryDTO entryDto = new PrescriptionEntryDTO();
        entryDto.setItemName("Paracetamol");
        entryDto.setDosage("500mg TDS");
        PrescriptionEntryDTO entry = prescriptionService.addEntryToMedicalHistory(medicalHistory.getId(), entryDto);
        assertNotNull(entry.getId(), "medication should be saved as a prescription entry");
        assertEquals("Paracetamol", entry.getItemName());
        em.flush();
        em.clear();

        // Nurse records a dose against that same entry.
        MedicationAdministrationDTO dose = new MedicationAdministrationDTO();
        dose.setPrescriptionEntryId(entry.getId());
        dose.setAdministrationDate(LocalDate.now());
        dose.setAdministrationTime(LocalTime.of(8, 0));
        dose.setAdministered(true);
        dose.setAdministeredBy("Nurse Test");
        MedicationAdministrationDTO saved = administrationService.record(dose);
        assertNotNull(saved.getId(), "dose should be persisted");
        em.flush();
        em.clear();

        List<MedicationAdministrationDTO> byHistory =
                administrationService.byMedicalHistory(medicalHistory.getId());
        assertEquals(1, byHistory.size(), "medical-history endpoint should return the dose");
        assertEquals(entry.getId(), byHistory.get(0).getPrescriptionEntryId(),
                "dose must be attached to the prescribed entry");

        List<MedicationAdministrationDTO> byEntry = administrationService.byEntry(entry.getId());
        assertEquals(1, byEntry.size(), "entry endpoint should return the dose");
    }
}
