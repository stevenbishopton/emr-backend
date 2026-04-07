package hospital.emr.notification.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {
    
    private String type; // "NEW_PATIENT", "URGENT_CASE", "PATIENT_READY", "RESULTS_READY", "EQUIPMENT_READY"
    private String title; // "New Patient Arrival", "Urgent Case Alert", etc.
    private String message; // Generic message without patient identifiers
    private String fromDepartment; // "front-desk", "lab", "radiology", etc.
    private String toDepartment; // Target department
    private String priority; // "LOW", "MEDIUM", "HIGH", "URGENT"
    private LocalDateTime timestamp;
    private String metadata;

    // Constructor without timestamp - will auto-set to current time
    public NotificationMessage(String type, String title, String message, 
                               String fromDepartment, String toDepartment, String priority) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.fromDepartment = fromDepartment;
        this.toDepartment = toDepartment;
        this.priority = priority;
        this.timestamp = LocalDateTime.now();
    }
}
