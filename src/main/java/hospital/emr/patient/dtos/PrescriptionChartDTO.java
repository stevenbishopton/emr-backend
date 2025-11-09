package hospital.emr.patient.dtos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PrescriptionChartDTO {
    private Long id;
    private Long admissionId;
    private List<AdmissionPrescriptionEntryDTO> entries;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}