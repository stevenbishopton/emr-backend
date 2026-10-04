package hospital.emr.lab.dtos;

import hospital.emr.lab.enums.LabPriority;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLabOrderRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long visitId;

    private Long medicalHistoryId;

    private LabPriority priority;

    private String clinicalNotes;

    @NotEmpty(message = "At least one test is required")
    @Valid
    private List<OrderItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull(message = "Test ID is required")
        private Long testId;
        private String notes;
    }
}
