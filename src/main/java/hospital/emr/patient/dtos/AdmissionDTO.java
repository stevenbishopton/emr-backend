package hospital.emr.patient.dtos;

import java.time.LocalDateTime;
import java.util.List;
import hospital.emr.common.dtos.NoteDTO;
import lombok.Data;

@Data
public class AdmissionDTO {
    private Long id;
    private Long patientId;
    private Long visitId;
    private String patientNames;
    private String patientCode;
    private Long wardId;
    private String wardName;
    private Long medicalHistoryId;
    private PrescriptionChartDTO prescriptionChartDTO;
    private List<NoteDTO>notes;
    private NoteDTO admissionRecord;
    private LocalDateTime admissionDate;
    private LocalDateTime dischargeDate;
}