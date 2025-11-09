package hospital.emr.reception.dtos;

import hospital.emr.reception.enums.VisitStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewVisitRequest {

    @NotNull
    private Long patientId;

    @NotNull
    private Long departmentId;

    @NotNull
    private VisitStatus status;

    private String notes;
}