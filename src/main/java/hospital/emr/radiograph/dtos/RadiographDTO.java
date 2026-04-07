package hospital.emr.radiograph.dtos;

import hospital.emr.radiograph.enums.RadiographType;
import hospital.emr.radiograph.enums.RadiographStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RadiographDTO {
    private Long id;
    private Long patientId;
    private Long visitId;
    private Long medicalHistoryId;
    private RadiographType radiographType;
    private String radiographName;
    private RadiographStatus status;
    private String carriedOutBy;
    private String interpretation;
    private String comments;
    private String orderDate;
    private String resultDate;
    
    // New fields
    private String scheduledDate;
    private String scheduledTime;
    private String technicianNotes;
    private String radiologistNotes;
    private String reportUrl;
    private String imageUrl;
    private Long requestedBy;
    private Long departmentId;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}