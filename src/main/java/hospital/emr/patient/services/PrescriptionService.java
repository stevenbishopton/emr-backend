package hospital.emr.patient.services;

import hospital.emr.patient.dtos.PrescriptionDTO;
import hospital.emr.patient.dtos.PrescriptionEntryDTO;
import hospital.emr.patient.entities.Prescription;
import hospital.emr.patient.entities.PrescriptionEntry;
import hospital.emr.patient.exceptions.MedicalHistoryNotFoundException;
import hospital.emr.patient.exceptions.PrescriptionNotFoundException;
import hospital.emr.patient.mapper.PrescriptionMapper;
import hospital.emr.patient.mapper.PrescriptionEntryMapper;
import hospital.emr.patient.repos.MedicalHistoryRepository;
import hospital.emr.patient.repos.PrescriptionRepository;
import hospital.emr.patient.repos.PrescriptionEntryRepository;
import hospital.emr.patient.repos.MedicationAdministrationRepository;
import hospital.emr.pharmacy.entities.Item;
import hospital.emr.pharmacy.exceptions.ItemNotFoundException;
import hospital.emr.pharmacy.repos.ItemRepository;
import hospital.emr.reception.entities.Visit;
import hospital.emr.reception.exceptions.VisitNotFoundException;
import hospital.emr.reception.repos.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final PrescriptionEntryMapper prescriptionEntryMapper;
    private final PrescriptionEntryRepository prescriptionEntryRepository;
    private final MedicationAdministrationRepository medicationAdministrationRepository;
    private final ItemRepository itemRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final VisitRepository visitRepository;

    /**
     * Append a single medication to the medical history's medication chart.
     * Creates the chart (prescription) if the patient has none yet. Used by the
     * Medication Administration Record so the MAR and the doctor's prescription
     * list share exactly the same entries.
     */
    @Transactional
    public PrescriptionEntryDTO addEntryToMedicalHistory(Long medicalHistoryId, PrescriptionEntryDTO entryDTO) {
        var medicalHistory = medicalHistoryRepository.findById(medicalHistoryId)
                .orElseThrow(() -> new MedicalHistoryNotFoundException(
                        "Medical History not found with ID: " + medicalHistoryId));

        Prescription prescription = prescriptionRepository.findAllByMedicalHistoryId(medicalHistoryId).stream()
                .filter(p -> !Boolean.TRUE.equals(p.getIsDischarge()))
                .max(Comparator.comparing(Prescription::getCreatedAt,
                        Comparator.nullsFirst(Comparator.naturalOrder())))
                .orElse(null);

        if (prescription == null) {
            prescription = new Prescription();
            prescription.setMedicalHistory(medicalHistory);
            prescription.setIsDischarge(false);
            prescription.setAdditionalInstructions("Inpatient medication chart");
            prescription.setPrescriptionEntries(new ArrayList<>());
        }
        if (prescription.getPrescriptionEntries() == null) {
            prescription.setPrescriptionEntries(new ArrayList<>());
        }

        PrescriptionEntry entry = new PrescriptionEntry();
        entry.setItemName(entryDTO.getItemName());
        entry.setDosage(entryDTO.getDosage());
        entry.setRoute(entryDTO.getRoute());
        if (entryDTO.getDurationDays() != null) {
            entry.setDurationDays(entryDTO.getDurationDays());
        }
        entry.setPrescription(prescription);
        prescription.getPrescriptionEntries().add(entry);

        Prescription saved = prescriptionRepository.save(prescription);
        PrescriptionEntry savedEntry = saved.getPrescriptionEntries()
                .get(saved.getPrescriptionEntries().size() - 1);
        return prescriptionEntryMapper.toDto(savedEntry);
    }

    @Transactional
    public PrescriptionEntryDTO updateEntry(Long entryId, PrescriptionEntryDTO dto) {
        PrescriptionEntry entry = prescriptionEntryRepository.findById(entryId)
                .orElseThrow(() -> new PrescriptionNotFoundException("Medication not found with ID: " + entryId));
        if (dto.getItemName() != null) entry.setItemName(dto.getItemName());
        if (dto.getDosage() != null) entry.setDosage(dto.getDosage());
        if (dto.getRoute() != null) entry.setRoute(dto.getRoute());
        if (dto.getDurationDays() != null) entry.setDurationDays(dto.getDurationDays());
        return prescriptionEntryMapper.toDto(prescriptionEntryRepository.save(entry));
    }

    @Transactional
    public void deleteEntry(Long entryId) {
        PrescriptionEntry entry = prescriptionEntryRepository.findById(entryId)
                .orElseThrow(() -> new PrescriptionNotFoundException("Medication not found with ID: " + entryId));
        medicationAdministrationRepository.deleteByPrescriptionEntry(entry);
        prescriptionEntryRepository.delete(entry);
    }


    @Transactional
    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
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

        // Build prescription - NO MORE PRESCRIBER CASTING!
        Prescription prescription = new Prescription();
        prescription.setPrescriberName(prescriptionDTO.getPrescriberName()); // Simply set the name as string
        prescription.setMedicalHistory(medicalHistory);
        prescription.setVisit(visit);
        prescription.setAdditionalInstructions(prescriptionDTO.getAdditionalInstructions());
        prescription.setIsDischarge(prescriptionDTO.getIsDischarge()); // FIX: Add missing isDischarge field

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
        // Set itemName from the persistent item to ensure it's always populated
        entry.setItemName(persistentItem.getName());
        return entry;
    }

    @Transactional
    public PrescriptionDTO updatePrescription(Long id, PrescriptionDTO prescriptionDTO) {
        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(PrescriptionNotFoundException::new);

        // Use mapper to update core fields
        prescriptionMapper.updateFromDto(prescriptionDTO, prescription);

        // Update prescriber name if provided
        if (prescriptionDTO.getPrescriberName() != null) {
            prescription.setPrescriberName(prescriptionDTO.getPrescriberName());
        }
        
        // Update isDischarge field if provided
        if (prescriptionDTO.getIsDischarge() != null) {
            prescription.setIsDischarge(prescriptionDTO.getIsDischarge());
        }

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

    @Transactional(readOnly = true)
    public List<PrescriptionDTO> getAllDischargePrescriptions() {
        return prescriptionRepository.findByIsDischarge(true).stream()
                .map(prescriptionMapper::toDto)
                .collect(Collectors.toList());
    }

}