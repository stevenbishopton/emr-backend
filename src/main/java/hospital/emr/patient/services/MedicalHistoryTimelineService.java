package hospital.emr.patient.services;

import hospital.emr.common.mappers.NoteMapper;
import hospital.emr.patient.dtos.VisitTimelineDTO;
import hospital.emr.patient.entities.MedicalHistory;
import hospital.emr.patient.mapper.AdmissionMapper;
import hospital.emr.patient.mapper.PrescriptionMapper;
import hospital.emr.patient.mapper.VitalSignsMapper;
import hospital.emr.patient.repos.MedicalHistoryRepository;
import hospital.emr.reception.entities.Visit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalHistoryTimelineService {

    private final MedicalHistoryRepository medicalHistoryRepository;
    private final NoteMapper noteMapper;
    private final PrescriptionMapper prescriptionMapper;
    private final VitalSignsMapper vitalSignsMapper;
    private final AdmissionMapper admissionMapper;

    @Transactional(readOnly = true)
    public List<VisitTimelineDTO> getTimelineForPatient(Long patientId) {
        MedicalHistory mh = medicalHistoryRepository.findByPatient_Id(patientId);
        // group by visits
        return mh.getPatient().getVisits().stream()
                .map(this::mapVisitToTimeline)
                .sorted(Comparator.comparing(VisitTimelineDTO::getVisitDateTime).reversed()) // newest first
                .collect(Collectors.toList());
    }

    private VisitTimelineDTO mapVisitToTimeline(Visit visit) {
        VisitTimelineDTO dto = new VisitTimelineDTO();
        dto.setVisitId(visit.getId());
        dto.setVisitDateTime(visit.getVisitDateTime());
        dto.setVisitStatus(visit.getStatus().name());
        dto.setNotes(visit.getNotes());

        // filter each type of history data by visitId if needed
        dto.setClinicalNotes(
                visit.getPatient().getMedicalHistory().getClinicalNotes().stream()
                        .filter(n -> n.getVisit() != null && n.getVisit().getId().equals(visit.getId()))
                        .map(noteMapper::toDto)
                        .collect(Collectors.toList())
        );

        dto.setDiagnosisNotes(
                visit.getPatient().getMedicalHistory().getDiagnosisNotes().stream()
                        .filter(n -> n.getVisit() != null && n.getVisit().getId().equals(visit.getId()))
                        .map(noteMapper::toDto)
                        .collect(Collectors.toList())
        );

        dto.setPrescriptions(
                visit.getPatient().getMedicalHistory().getPrescriptions().stream()
                        .filter(p -> p.getVisit() != null && p.getVisit().getId().equals(visit.getId()))
                        .map(prescriptionMapper::toDto)
                        .collect(Collectors.toList())
        );

        dto.setVitals(
                visit.getPatient().getMedicalHistory().getVitalSignsList().stream()
                        .filter(v -> v.getVisit() != null && v.getVisit().getId().equals(visit.getId()))
                        .map(vitalSignsMapper::toDto)
                        .collect(Collectors.toList())
        );

        dto.setAdmissions(
                visit.getPatient().getMedicalHistory().getAdmissions().stream()
                        .filter(a -> a.getVisit() != null && a.getVisit().getId().equals(visit.getId()))
                        .map(admissionMapper::toDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
