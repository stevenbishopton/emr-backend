package hospital.emr.patient.dtos;

import hospital.emr.common.dtos.NoteDTO;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class VisitMedicalHistoryDTO {
    private Long visitId;
    private List<NoteDTO> clinicalNotes = new ArrayList<>();
    private List<NoteDTO> diagnosisNotes = new ArrayList<>();
    private List<PrescriptionDTO> prescriptions = new ArrayList<>();
    private List<VitalSignsDTO> vitalSigns = new ArrayList<>();
    private AdmissionDTO admission;

    public VisitMedicalHistoryDTO(Long visitId) {
        this.visitId = visitId;
    }

    // Helper to check if this visit has any data
    public boolean hasData() {
        return !clinicalNotes.isEmpty() ||
                !diagnosisNotes.isEmpty() ||
                !prescriptions.isEmpty() ||
                !vitalSigns.isEmpty() ||
                admission != null;
    }
}