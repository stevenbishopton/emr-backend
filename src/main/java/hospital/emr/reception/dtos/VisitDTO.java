package hospital.emr.reception.dtos;

import hospital.emr.reception.enums.VisitStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class VisitDTO {
    private Long id;

    private String visitDateTime;

    @NotNull
    private VisitStatus status;

    private String notes;

    private Long patientId;

    private String patientName;

    private String patientCode;

    private Set<VisitDepartmentDTO> visitDepartments;
}
