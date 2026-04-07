package hospital.emr.lab.dtos;

import hospital.emr.lab.enums.TestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating a lab test result
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLabTestResultRequest {
    private String results;
    private String interpretation;
    private String comments;
    private TestStatus status;
}
