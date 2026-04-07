package hospital.emr.lab.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabTestRequestResponseDTO {
    private Long id;
    private Long patientId;
    private Long visitId;
    private Long medicalHistoryId;
    private List<Long> labTestIds;
    private String requestedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String comments;
}