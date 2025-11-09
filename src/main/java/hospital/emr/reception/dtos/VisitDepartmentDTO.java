package hospital.emr.reception.dtos;

import hospital.emr.reception.enums.VisitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitDepartmentDTO {

    private Long departmentId;
    private String departmentName;
    private Long visitId;
    private Long handledByPersonnelId;
    private LocalDateTime assignedAt;

    private String notes;
    private VisitStatus status;
}
