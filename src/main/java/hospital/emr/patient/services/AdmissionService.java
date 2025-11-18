package hospital.emr.patient.services;

import hospital.emr.common.dtos.NoteDTO;
import hospital.emr.common.entities.Note;
import hospital.emr.common.enums.NoteType;
import hospital.emr.common.mappers.NoteMapper;
import hospital.emr.common.repos.NoteRepository;
import hospital.emr.patient.dtos.AdmissionDTO;
import hospital.emr.patient.entities.Admission;
import hospital.emr.patient.entities.PrescriptionChart;
import hospital.emr.patient.exceptions.MedicalHistoryNotFoundException;
import hospital.emr.patient.mapper.AdmissionMapper;
import hospital.emr.patient.repos.AdmissionRepository;
import hospital.emr.patient.repos.MedicalHistoryRepository;
import hospital.emr.patient.repos.PrescriptionChartRepository;
import hospital.emr.reception.exceptions.VisitNotFoundException;
import hospital.emr.reception.repos.VisitRepository;
import hospital.emr.reception.services.VisitService;
import hospital.emr.ward.exceptions.WardNotFoundException;
import hospital.emr.ward.repos.WardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdmissionService {
    private final AdmissionRepository admissionRepository;
    private final AdmissionMapper admissionMapper;
    private final NoteMapper noteMapper;
    private final WardRepository wardRepository;
    private final VisitRepository visitRepository;
    private final NoteRepository noteRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final VisitService visitService;
    private final PrescriptionChartRepository prescriptionChartRepository;

    @Transactional
    public AdmissionDTO admitPatient(AdmissionDTO admissionDto) {
        // Admits a patient by creating admission record with prescription chart and notes
        if (admissionDto.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID is required for admission.");
        }

        // ENFORCE SINGLE ACTIVE ADMISSION RULE
        Optional<Admission> existingActiveAdmission = admissionRepository
                .findTopByMedicalHistory_Patient_IdAndDischargeDateIsNullOrderByAdmissionDateDesc(admissionDto.getPatientId());

        if (existingActiveAdmission.isPresent()) {
            throw new IllegalStateException(
                    "Patient already has an active admission (ID: " + existingActiveAdmission.get().getId() + "). " +
                            "Patient must be discharged before creating a new admission."
            );
        }

        // Validate admission date
        if (admissionDto.getAdmissionDate() == null) {
            throw new IllegalArgumentException("Admission date is required.");
        }

        var medicalHistory = medicalHistoryRepository.findByPatient_Id(admissionDto.getPatientId());
        if (medicalHistory == null) {
            throw new MedicalHistoryNotFoundException(
                    "No medical history found for patient ID: " + admissionDto.getPatientId()
            );
        }

        if (admissionDto.getVisitId() == null) {
            throw new VisitNotFoundException("Visit ID is required for admission.");
        }

        visitService.makeVisitAdmission(admissionDto.getVisitId());
        var visit = visitRepository.findById(admissionDto.getVisitId())
                .orElseThrow(() -> new VisitNotFoundException(
                        "Visit not found with ID: " + admissionDto.getVisitId()
                ));

        Admission admission = new Admission();
        admission.setMedicalHistory(medicalHistory);
        admission.setVisit(visit);
        admission.setAdmissionDate(admissionDto.getAdmissionDate());

        // If discharge date is provided on admission (for back-dated admissions)
        if (admissionDto.getDischargeDate() != null) {
            if (admissionDto.getDischargeDate().isBefore(admissionDto.getAdmissionDate())) {
                throw new IllegalArgumentException("Discharge date cannot be before admission date.");
            }
            admission.setDischargeDate(admissionDto.getDischargeDate());
        }

        if (admissionDto.getWardId() != null) {
            var ward = wardRepository.findById(admissionDto.getWardId())
                    .orElseThrow(() -> new WardNotFoundException(
                            "Ward not found with ID: " + admissionDto.getWardId()
                    ));
            admission.setWard(ward);
        }

        // Create admission record note BEFORE saving admission if provided
        Note admissionRecordNote = null;
        if (admissionDto.getAdmissionRecord() != null) {
            var admissionNoteDto = admissionDto.getAdmissionRecord();
            admissionNoteDto.setMedicalHistoryId(medicalHistory.getId());
            admissionNoteDto.setVisitId(visit.getId());

            var createdNote = noteRepository.save(noteMapper.toEntity(admissionNoteDto));
            admissionRecordNote = createdNote;
        }

        // Set the admission record and save admission
        admission.setAdmissionRecord(admissionRecordNote);
        Admission savedAdmission = admissionRepository.save(admission);

        PrescriptionChart chart = PrescriptionChart.builder()
                .admission(savedAdmission)
                .entries(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        prescriptionChartRepository.save(chart);

        return admissionMapper.toDto(savedAdmission);
    }

    @Transactional(readOnly = true)
    public Page<AdmissionDTO> getAllAdmissionsHistorically(Pageable pageable) {
        // Returns paginated list of all admissions (historical and active)
        return admissionRepository.findAll(pageable).map(admissionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<AdmissionDTO> getAdmissionsByWardHistorically(Long wardId) {
        // Returns all admissions for a specific ward regardless of discharge status
        return admissionRepository.findByWard_Id(wardId)
                .stream()
                .map(admissionMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdmissionDTO getAdmissionById(Long admissionId) {
        // Returns specific admission details by admission ID
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new EntityNotFoundException("Admission not found with ID: " + admissionId));
        return admissionMapper.toDto(admission);
    }

    @Transactional(readOnly = true)
    public List<AdmissionDTO> getAllAdmissionsByPatientId(Long patientId) {
        // Returns ALL admissions for a patient (historical and active)
        return admissionRepository.findByMedicalHistory_Patient_IdOrderByAdmissionDateDesc(patientId)
                .stream()
                .map(admissionMapper::toDto)
                .toList();
    }

    @Transactional
    public AdmissionDTO updateAdmission(AdmissionDTO admissionDto) {
        Admission existingAdmission = admissionRepository.findById(admissionDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Admission not found with ID: " + admissionDto.getId()
                ));

        // Update admission date if provided
        if (admissionDto.getAdmissionDate() != null) {
            existingAdmission.setAdmissionDate(admissionDto.getAdmissionDate());
        }

        // Update ward if provided
        if (admissionDto.getWardId() != null) {
            var ward = wardRepository.findById(admissionDto.getWardId())
                    .orElseThrow(() -> new WardNotFoundException(
                            "Ward not found with ID: " + admissionDto.getWardId()
                    ));
            existingAdmission.setWard(ward);
        } else if (admissionDto.getWardId() == null && existingAdmission.getWard() != null) {
            existingAdmission.setWard(null);
        }

        // ✅ NEW: Handle admission record update
        if (admissionDto.getAdmissionRecord() != null) {
            updateAdmissionRecord(existingAdmission, admissionDto.getAdmissionRecord());
        }

        // Handle discharge date
        if (admissionDto.getDischargeDate() != null) {
            LocalDateTime admissionDate = admissionDto.getAdmissionDate() != null ?
                    admissionDto.getAdmissionDate() : existingAdmission.getAdmissionDate();

            if (admissionDto.getDischargeDate().isBefore(admissionDate)) {
                throw new IllegalArgumentException("Discharge date cannot be before admission date.");
            }
            existingAdmission.setDischargeDate(admissionDto.getDischargeDate());

            if (existingAdmission.getVisit() != null) {
                visitService.markVisitAsCompleted(existingAdmission.getVisit().getId());
            }
        }

        Admission updatedAdmission = admissionRepository.save(existingAdmission);
        log.info("Admission with ID {} updated successfully", admissionDto.getId());
        return admissionMapper.toDto(updatedAdmission);
    }

    // ✅ NEW: Helper method to handle admission record updates
    private void updateAdmissionRecord(Admission admission, NoteDTO admissionRecordDto) {
        Note existingAdmissionRecord = admission.getAdmissionRecord();

        if (existingAdmissionRecord != null) {
            // Update existing admission record
            if (admissionRecordDto.getContent() != null) {
                existingAdmissionRecord.setContent(admissionRecordDto.getContent());
            }
            if (admissionRecordDto.getAuthor() != null) {
                existingAdmissionRecord.setAuthor(admissionRecordDto.getAuthor());
            }
            if (admissionRecordDto.getNoteType() != null) {
                existingAdmissionRecord.setNoteType(admissionRecordDto.getNoteType());
            }
            // Note is automatically saved when admission is saved due to cascade or manual save
        } else {
            // Create new admission record
            Note newAdmissionRecord = noteMapper.toEntity(admissionRecordDto);
            newAdmissionRecord.setNoteType(NoteType.ADMISSION);
            newAdmissionRecord.setMedicalHistory(admission.getMedicalHistory());
            newAdmissionRecord.setVisit(admission.getVisit());

            Note savedNote = noteRepository.save(newAdmissionRecord);
            admission.setAdmissionRecord(savedNote);
        }
    }

    // ✅ NEW: Special method to update only admission record
    @Transactional
    public AdmissionDTO updateAdmissionRecord(Long admissionId, NoteDTO admissionRecordDto) {
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Admission not found with ID: " + admissionId
                ));

        updateAdmissionRecord(admission, admissionRecordDto);

        Admission updatedAdmission = admissionRepository.save(admission);
        log.info("Admission record updated for admission ID: {}", admissionId);
        return admissionMapper.toDto(updatedAdmission);
    }

    @Transactional(readOnly = true)
    public List<AdmissionDTO> getActiveAdmissions() {
        // Returns all currently admitted patients (no discharge date)
        return admissionRepository.findByDischargeDateIsNull()
                .stream()
                .map(admissionMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AdmissionDTO> getActiveAdmissionsByWard(Long wardId) {
        // Returns currently admitted patients filtered by specific ward
        return admissionRepository.findByWard_IdAndDischargeDateIsNull(wardId)
                .stream()
                .map(admissionMapper::toDto)
                .toList();
    }

    @Transactional
    public AdmissionDTO dischargePatient(Long admissionId, LocalDateTime dischargeDate) {
        // Discharges patient with specific discharge date
        Admission admission = admissionRepository.findById(admissionId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Admission not found with ID: " + admissionId
                ));

        // Validate discharge date is after admission date
        if (dischargeDate.isBefore(admission.getAdmissionDate())) {
            throw new IllegalArgumentException("Discharge date cannot be before admission date.");
        }

        admission.setDischargeDate(dischargeDate);

        if (admission.getVisit() != null) {
            visitService.markVisitAsCompleted(admission.getVisit().getId());
        }

        Admission dischargedAdmission = admissionRepository.save(admission);
        log.info("Patient discharged from admission ID: {} on {}", admissionId, dischargeDate);

        return admissionMapper.toDto(dischargedAdmission);
    }

    @Transactional(readOnly = true)
    public boolean isPatientAdmitted(Long patientId) {
        // Checks if patient has ANY active admissions
        return admissionRepository.existsByMedicalHistory_Patient_IdAndDischargeDateIsNull(patientId);
    }

    @Transactional(readOnly = true)
    public List<AdmissionDTO> getAdmissionsInDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        // Returns admissions within a specific date range
        return admissionRepository.findByAdmissionDateBetween(startDate, endDate)
                .stream()
                .map(admissionMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdmissionDTO getAdmissionByVisitId(Long visitId){
        Admission admission = admissionRepository.findByVisit_Id(visitId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Admission not found with ID: " + visitId
                ));
        return admissionMapper.toDto(admission);
    }
}