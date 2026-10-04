package hospital.emr.lab.dtos;

import hospital.emr.lab.enums.LabOrderStatus;
import hospital.emr.lab.enums.LabPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabOrderDTO {
    private Long id;
    private Long patientId;
    private Long visitId;
    private Long medicalHistoryId;
    private LabOrderStatus status;
    private LabPriority priority;
    private String clinicalNotes;
    private Long orderedById;
    private String orderedByName;
    private Boolean criticalAcknowledged;
    private Long criticalAcknowledgedById;
    private String criticalAcknowledgedByName;
    private LocalDateTime criticalAcknowledgedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<LabOrderItemDTO> items;
    private List<LabSpecimenDTO> specimens;
    private List<LabOrderAuditDTO> auditTrail;
}
