package hospital.emr.lab.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLabTestRequestDTO {
    private Long patientId;
    private Long visitId;
    private Long medicalHistoryId;
    private List<Long> labTestIds;
    private String requestedBy;
    private String comments;
}