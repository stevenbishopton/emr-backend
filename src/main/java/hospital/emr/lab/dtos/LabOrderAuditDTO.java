package hospital.emr.lab.dtos;

import hospital.emr.lab.enums.LabOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabOrderAuditDTO {
    private Long id;
    private String action;
    private LabOrderStatus fromStatus;
    private LabOrderStatus toStatus;
    private Long actorId;
    private String actorName;
    private String notes;
    private LocalDateTime timestamp;
}
