package hospital.emr.patient.dtos;

import java.time.LocalDateTime;
import java.util.List;

import hospital.emr.common.dtos.NoteDTO;
import lombok.Data;

@Data
public class VisitTimelineDTO {
    private Long visitId;
    private LocalDateTime visitDateTime;
    private String visitStatus;
    private String notes;

    private List<NoteDTO> clinicalNotes;
    private List<NoteDTO> diagnosisNotes;
    private List<PrescriptionDTO> prescriptions;
    private List<VitalSignsDTO> vitals;
    private List<AdmissionDTO> admissions;
}
