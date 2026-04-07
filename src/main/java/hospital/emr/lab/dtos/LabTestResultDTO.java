package hospital.emr.lab.dtos;

import hospital.emr.lab.enums.TestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for LabTestResult entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabTestResultDTO {
    private Long id;
    private Long patientId;
    private Long visitId;
    private String requestedBy;
    private List<Long> testIds;
    private List<LabTestDTO> tests; // Populated with test details
    private String results;
    private String interpretation;
    private String comments;
    private TestStatus status;
    private String carriedOutBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;
}
