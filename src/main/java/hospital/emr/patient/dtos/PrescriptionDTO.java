package hospital.emr.patient.dtos;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PrescriptionDTO {
    private Long id;
    private List<PrescriptionEntryDTO> prescriptionEntries;
    private String additionalInstructions;
    private String prescriberName;
    private Long visitId;
    private Long medicalHistoryId;
    private Boolean isDischarge;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}