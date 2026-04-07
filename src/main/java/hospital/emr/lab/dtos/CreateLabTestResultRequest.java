package hospital.emr.lab.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for creating a new lab test result
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLabTestResultRequest {
    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long visitId;

    private String requestedBy;

    @NotEmpty(message = "At least one test ID is required")
    private List<Long> testIds;

    private String results;

    private String interpretation;

    private String comments;

    private String carriedOutBy;
}
