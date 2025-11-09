package hospital.emr.patient.services;

import hospital.emr.patient.dtos.VitalSignsDTO;
import hospital.emr.patient.entities.MedicalHistory;
import hospital.emr.patient.entities.Patient;
import hospital.emr.patient.entities.VitalSigns;
import hospital.emr.patient.exceptions.MedicalHistoryNotFoundException;
import hospital.emr.patient.exceptions.PatientNotFoundException;
import hospital.emr.patient.mapper.VitalSignsMapper;
import hospital.emr.patient.repos.MedicalHistoryRepository;
import hospital.emr.patient.repos.PatientRepository;
import hospital.emr.patient.repos.VitalSignsRepository;
import hospital.emr.reception.entities.Visit;
import hospital.emr.reception.repos.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VitalSignsService {

    private final VitalSignsRepository vitalSignsRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final VitalSignsMapper vitalSignsMapper;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    /**
     * Record new vital signs for a patient.
     */
    @Transactional
    public VitalSignsDTO recordVitalSigns(VitalSignsDTO dto) {
        if (dto.getVisitId() == null) {
            throw new IllegalArgumentException("Visit ID is required.");
        }

        Visit visit = visitRepository.findById(dto.getVisitId())
                .orElseThrow(() -> new IllegalArgumentException("Visit not found: " + dto.getVisitId()));

        VitalSigns entity = vitalSignsMapper.toEntity(dto);
        entity.setVisit(visit);

        // Attach patient's medical history from the visit → patient link
        Patient patient = visit.getPatient();
        if (patient.getMedicalHistory() != null) {
            entity.setMedicalHistory(patient.getMedicalHistory());
        } else {
            throw new MedicalHistoryNotFoundException("No medical history found for patient " + patient.getId());
        }

        entity.setTimeTaken(LocalDateTime.now());
        VitalSigns saved = vitalSignsRepository.save(entity);

        return vitalSignsMapper.toDto(saved);
    }

    /**
     * Get all vitals for a specific medical history.
     */
    public List<VitalSignsDTO> getVitalsByMedicalHistory(Long medicalHistoryId) {
        return vitalSignsRepository.findByMedicalHistory_Id(medicalHistoryId).stream()
                .map(vitalSignsMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get the latest vital signs for a patient by medical history ID.
     */
    public Optional<VitalSignsDTO> getLatestVitalsByMedicalHistory(Long medicalHistoryId) {
        return vitalSignsRepository.findTopByMedicalHistory_IdOrderByTimeTakenDesc(medicalHistoryId)
                .map(vitalSignsMapper::toDto);
    }

    /**
     * Get the latest vital signs for a patient by visit ID.
     */
    public Optional<VitalSignsDTO> getLatestVitalsByVisit(Long visitId) {
        return vitalSignsRepository.findTopByVisit_IdOrderByTimeTakenDesc(visitId)
                .map(vitalSignsMapper::toDto);
    }

    /**
     * Get the latest vital signs for a patient by patient ID.
     */
    public Optional<VitalSignsDTO> getLatestVitalsByPatient(Long patientId) {
        // First get the medical history for the patient
        MedicalHistory medicalHistory = medicalHistoryRepository.findByPatient_Id(patientId);
        if (medicalHistory == null) {
            return Optional.empty();
        }

        return vitalSignsRepository.findTopByMedicalHistory_IdOrderByTimeTakenDesc(medicalHistory.getId())
                .map(vitalSignsMapper::toDto);
    }

    public List<VitalSignsDTO> getVitalsByVisit(Long visitId) {
        return vitalSignsRepository.findByVisit_Id(visitId).stream()
                .map(vitalSignsMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get single vital sign record.
     */
    public VitalSignsDTO getVitalSignsById(Long id) {
        VitalSigns entity = vitalSignsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("VitalSigns not found"));
        return vitalSignsMapper.toDto(entity);
    }

    /**
     * Delete a vital sign record.
     */
    public void deleteVitalSigns(Long id) {
        if (!vitalSignsRepository.existsById(id)) {
            throw new IllegalArgumentException("VitalSigns not found");
        }
        vitalSignsRepository.deleteById(id);
    }
}