package hospital.emr.radiograph.dtos;

import hospital.emr.radiograph.enums.RadiographStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RadiographHistoryDTO {
    private Long id;
    private Long radiographId;
    private RadiographStatus status;
    private String notes;
    private Long performedBy;
    private String performedByName;
    private LocalDateTime timestamp;
    private String previousStatus;
    private String reason;
    private String departmentName;
}
