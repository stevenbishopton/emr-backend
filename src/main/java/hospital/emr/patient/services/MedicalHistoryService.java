package hospital.emr.patient.services;

import hospital.emr.common.dtos.NoteDTO;
import hospital.emr.common.entities.Note;
import hospital.emr.common.mappers.NoteMapper;
import hospital.emr.patient.dtos.MedicalHistoryDTO;
import hospital.emr.patient.dtos.PatientDTO;
import hospital.emr.patient.dtos.VisitMedicalHistoryDTO;
import hospital.emr.patient.entities.*;
import hospital.emr.patient.mapper.AdmissionMapper;
import hospital.emr.patient.mapper.MedicalHistoryMapper;
import hospital.emr.patient.mapper.PrescriptionMapper;
import hospital.emr.patient.mapper.VitalSignsMapper;
import hospital.emr.patient.repos.MedicalHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MedicalHistoryService {
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final MedicalHistoryMapper medicalHistoryMapper;
    private final NoteMapper noteMapper;
    private final PrescriptionMapper prescriptionMapper;
    private final VitalSignsMapper vitalSignsMapper;
    private final AdmissionMapper admissionMapper;

    @Transactional
    public MedicalHistoryDTO createMedicalHistory(MedicalHistoryDTO medicalHistoryDTO){
        MedicalHistory medicalHistory = medicalHistoryMapper.toEntity(medicalHistoryDTO);
        medicalHistoryRepository.save(medicalHistory);
        return medicalHistoryMapper.toDto(medicalHistory);
    }

    @Transactional
    public MedicalHistoryDTO getMedicalHistory(Long patientId){
        MedicalHistory medicalHistory = medicalHistoryRepository.findByPatient_Id(patientId);
        return medicalHistoryMapper.toDto(medicalHistory);
    }

    // Get medical history grouped by visits - CORRECTED VERSION
    @Transactional(readOnly = true)
    public List<VisitMedicalHistoryDTO> getMedicalHistoryByVisits(Long patientId) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findByPatient_Id(patientId);

        if (medicalHistory == null) {
            return new ArrayList<>();
        }

        Map<Long, VisitMedicalHistoryDTO> visitsMap = new HashMap<>();

        // Process clinical notes - FIXED: get visit ID from Visit object
        if (medicalHistory.getClinicalNotes() != null) {
            for (Note note : medicalHistory.getClinicalNotes()) {
                if (note.getVisit() != null && note.getVisit().getId() != null) {
                    Long visitId = note.getVisit().getId();
                    VisitMedicalHistoryDTO visitDTO = visitsMap.computeIfAbsent(visitId,
                            k -> new VisitMedicalHistoryDTO(visitId));
                    visitDTO.getClinicalNotes().add(noteMapper.toDto(note));
                }
            }
        }

        // Process diagnosis notes - FIXED: get visit ID from Visit object
        if (medicalHistory.getDiagnosisNotes() != null) {
            for (Note note : medicalHistory.getDiagnosisNotes()) {
                if (note.getVisit() != null && note.getVisit().getId() != null) {
                    Long visitId = note.getVisit().getId();
                    VisitMedicalHistoryDTO visitDTO = visitsMap.computeIfAbsent(visitId,
                            k -> new VisitMedicalHistoryDTO(visitId));
                    visitDTO.getDiagnosisNotes().add(noteMapper.toDto(note));
                }
            }
        }

        // Process prescriptions - FIXED: get visit ID from Visit object
        if (medicalHistory.getPrescriptions() != null) {
            for (Prescription prescription : medicalHistory.getPrescriptions()) {
                if (prescription.getVisit() != null && prescription.getVisit().getId() != null) {
                    Long visitId = prescription.getVisit().getId();
                    VisitMedicalHistoryDTO visitDTO = visitsMap.computeIfAbsent(visitId,
                            k -> new VisitMedicalHistoryDTO(visitId));
                    visitDTO.getPrescriptions().add(prescriptionMapper.toDto(prescription));
                }
            }
        }

        // Process vital signs - FIXED: get visit ID from Visit object
        if (medicalHistory.getVitalSignsList() != null) {
            for (VitalSigns vitalSigns : medicalHistory.getVitalSignsList()) {
                if (vitalSigns.getVisit() != null && vitalSigns.getVisit().getId() != null) {
                    Long visitId = vitalSigns.getVisit().getId();
                    VisitMedicalHistoryDTO visitDTO = visitsMap.computeIfAbsent(visitId,
                            k -> new VisitMedicalHistoryDTO(visitId));
                    visitDTO.getVitalSigns().add(vitalSignsMapper.toDto(vitalSigns));
                }
            }
        }

        // Process admissions - FIXED: get visit ID from Visit object
        if (medicalHistory.getAdmissions() != null) {
            for (Admission admission : medicalHistory.getAdmissions()) {
                if (admission.getVisit() != null && admission.getVisit().getId() != null) {
                    Long visitId = admission.getVisit().getId();
                    VisitMedicalHistoryDTO visitDTO = visitsMap.computeIfAbsent(visitId,
                            k -> new VisitMedicalHistoryDTO(visitId));
                    visitDTO.setAdmission(admissionMapper.toDto(admission));
                }
            }
        }

        // Return sorted by visitId (most recent first)
        return visitsMap.values().stream()
                .sorted((v1, v2) -> v2.getVisitId().compareTo(v1.getVisitId()))
                .collect(Collectors.toList());
    }
}

