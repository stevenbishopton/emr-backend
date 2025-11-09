package hospital.emr.patient.services;

import hospital.emr.doctor.entities.Doctor;
import hospital.emr.doctor.exceptions.DoctorNotFoundException;
import hospital.emr.patient.dtos.PrescriptionDTO;
import hospital.emr.patient.dtos.PrescriptionEntryDTO;
import hospital.emr.patient.entities.Prescription;
import hospital.emr.patient.entities.PrescriptionEntry;
import hospital.emr.patient.exceptions.MedicalHistoryNotFoundException;
import hospital.emr.patient.exceptions.PrescriptionNotFoundException;
import hospital.emr.patient.mapper.PrescriptionMapper;
import hospital.emr.patient.repos.MedicalHistoryRepository;
import hospital.emr.patient.repos.PrescriptionRepository;
import hospital.emr.pharmacy.entities.Item;
import hospital.emr.pharmacy.exceptions.ItemNotFoundException;
import hospital.emr.pharmacy.repos.ItemRepository;
import hospital.emr.reception.entities.Visit;
import hospital.emr.reception.exceptions.VisitNotFoundException;
import hospital.emr.reception.repos.VisitRepository;
import hospital.emr.common.repos.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final ItemRepository itemRepository;
    private final PersonnelRepository personnelRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final VisitRepository visitRepository;

    @Transactional
    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
        // Fetch prescriber
        var prescriber = personnelRepository.findById(prescriptionDTO.getPrescriberId())
                .orElseThrow(() -> new DoctorNotFoundException(
                        "Doctor not found with ID: " + prescriptionDTO.getPrescriberId()));

        // Fetch medical history
        var medicalHistory = medicalHistoryRepository.findById(prescriptionDTO.getMedicalHistoryId())
                .orElseThrow(() -> new MedicalHistoryNotFoundException(
                        "Medical History not found with ID: " + prescriptionDTO.getMedicalHistoryId()));

        // Optionally fetch visit
        Visit visit = null;
        if (prescriptionDTO.getVisitId() != null) {
            visit = visitRepository.findById(prescriptionDTO.getVisitId())
                    .orElseThrow(() -> new VisitNotFoundException(
                            "Visit not found with ID: " + prescriptionDTO.getVisitId()));
        }

        // Build prescription
        Prescription prescription = new Prescription();
        prescription.setPrescriber((Doctor) prescriber);
        prescription.setMedicalHistory(medicalHistory);
        prescription.setVisit(visit);
        prescription.setAdditionalInstructions(prescriptionDTO.getAdditionalInstructions());

        // Process entries
        if (prescriptionDTO.getPrescriptionEntries() != null) {
            prescription.setPrescriptionEntries(
                    prescriptionDTO.getPrescriptionEntries().stream()
                            .map(entryDTO -> {
                                Item persistentItem = itemRepository.findById(entryDTO.getItemId())
                                        .orElseThrow(() -> new ItemNotFoundException(
                                                "Item not found with ID: " + entryDTO.getItemId()));

                                return getPrescriptionEntry(entryDTO, persistentItem, prescription);
                            })
                            .collect(Collectors.toList())
            );
        }
        // Save
        Prescription savedPrescription = prescriptionRepository.save(prescription);

        return prescriptionMapper.toDto(savedPrescription);
    }

    private static PrescriptionEntry getPrescriptionEntry(
            PrescriptionEntryDTO entryDTO,
            Item persistentItem,
            Prescription prescription
    ) {
        PrescriptionEntry entry = new PrescriptionEntry();
        entry.setDosage(entryDTO.getDosage());
        entry.setRoute(entryDTO.getRoute());
        entry.setDurationDays(entryDTO.getDurationDays());
        entry.setItem(persistentItem);
        entry.setPrescription(prescription);
        return entry;
    }

    @Transactional
    public PrescriptionDTO updatePrescription(Long id, PrescriptionDTO prescriptionDTO) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(PrescriptionNotFoundException::new);

        // Use mapper to update core fields
        prescriptionMapper.updateFromDto(prescriptionDTO, prescription);

        // Handle entries manually if needed
        if (prescriptionDTO.getPrescriptionEntries() != null) {
            prescription.getPrescriptionEntries().clear();
            prescription.getPrescriptionEntries().addAll(
                    prescriptionDTO.getPrescriptionEntries().stream()
                            .map(entryDTO -> {
                                Item persistentItem = itemRepository.findById(entryDTO.getItemId())
                                        .orElseThrow(() -> new ItemNotFoundException(
                                                "Item not found with ID: " + entryDTO.getItemId()));
                                return getPrescriptionEntry(entryDTO, persistentItem, prescription);
                            })
                            .collect(Collectors.toList())
            );
        }

        Prescription updatedPrescription = prescriptionRepository.save(prescription);
        return prescriptionMapper.toDto(updatedPrescription);
    }

    @Transactional
    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(PrescriptionNotFoundException::new);
        prescriptionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PrescriptionDTO findPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(PrescriptionNotFoundException::new);
        return prescriptionMapper.toDto(prescription);
    }

    @Transactional(readOnly = true)
    public PrescriptionDTO findMostRecentPrescriptionByPatientId(Long patientId) {
        Prescription prescription = prescriptionRepository
                .findTopByMedicalHistory_Patient_IdOrderByCreatedAtDesc(patientId)
                .orElseThrow(() -> new PrescriptionNotFoundException(
                        "No prescription found for patient with ID: " + patientId));
        return prescriptionMapper.toDto(prescription);
    }


    @Transactional(readOnly = true)
    public List<PrescriptionDTO> findAllPrescriptionByMedicalHistoryId(Long id) {
        List<Prescription> prescriptions = prescriptionRepository.findAllByMedicalHistoryId(id);
        return prescriptions.stream()
                .map(prescriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(prescriptionMapper::toDto)
                .collect(Collectors.toList());
    }
}
