package hospital.emr.patient.dtos;

import java.util.List;

import hospital.emr.common.dtos.NoteDTO;
import lombok.Data;

@Data
public class MedicalHistoryDTO {
    private Long id;
    private Long patientId;
    private String patientNames;
    private String patientCode;
    private List<NoteDTO> diagnosisNotes;
    private List<NoteDTO> clinicalNotes;
    private List<PrescriptionDTO> prescriptions;
    private List<VitalSignsDTO> vitalSignsList;
    private List<AdmissionDTO> admissions; // A simplified DTO for admissions
}