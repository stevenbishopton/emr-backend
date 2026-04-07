package hospital.emr.notification.controller;

import hospital.emr.notification.model.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications/department")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/{toDepartment}/new-patient")
    public ResponseEntity<Void> notifyNewPatient(
            @PathVariable String toDepartment,
            @RequestParam String fromDepartment) {
        log.info("📢 Sending new patient notification from {} to {}", fromDepartment, toDepartment);
        sendDepartmentNotification(fromDepartment, toDepartment, "NEW_PATIENT",
                "New Patient Arrival", "A new patient has arrived and is ready for processing.", "MEDIUM");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{toDepartment}/patient-ready")
    public ResponseEntity<Void> notifyPatientReady(
            @PathVariable String toDepartment,
            @RequestParam String fromDepartment) {
        log.info("📢 Sending patient ready notification from {} to {}", fromDepartment, toDepartment);
        sendDepartmentNotification(fromDepartment, toDepartment, "PATIENT_READY",
                "Patient Ready", "A patient is ready for their next step.", "MEDIUM");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{toDepartment}/urgent-case")
    public ResponseEntity<Void> notifyUrgentCase(
            @PathVariable String toDepartment,
            @RequestParam String fromDepartment) {
        log.info("📢 Sending urgent case notification from {} to {}", fromDepartment, toDepartment);
        sendDepartmentNotification(fromDepartment, toDepartment, "URGENT_CASE",
                "Urgent Case Alert", "An urgent case requires immediate attention.", "URGENT");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{toDepartment}/results-ready")
    public ResponseEntity<Void> notifyTestResultsReady(
            @PathVariable String toDepartment,
            @RequestParam String fromDepartment) {
        log.info("📢 Sending results ready notification from {} to {}", fromDepartment, toDepartment);
        sendDepartmentNotification(fromDepartment, toDepartment, "RESULTS_READY",
                "Test Results Ready", "Lab or radiology results are available.", "HIGH");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{toDepartment}/equipment-ready")
    public ResponseEntity<Void> notifyEquipmentReady(
            @PathVariable String toDepartment,
            @RequestParam String fromDepartment) {
        log.info("📢 Sending equipment ready notification from {} to {}", fromDepartment, toDepartment);
        sendDepartmentNotification(fromDepartment, toDepartment, "EQUIPMENT_READY",
                "Equipment Ready", "Requested equipment is now available.", "LOW");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{toDepartment}")
    public ResponseEntity<Void> sendGenericDepartmentNotification(
            @PathVariable String toDepartment,
            @RequestBody NotificationMessage notificationMessage) {
        log.info("📢 Sending generic notification to {}: {}", toDepartment, notificationMessage);

        // Ensure timestamp is set if not provided in request body
        if (notificationMessage.getTimestamp() == null) {
            notificationMessage.setTimestamp(java.time.LocalDateTime.now());
        }

        sendDepartmentNotification(
                notificationMessage.getFromDepartment(),
                toDepartment,
                notificationMessage.getType(),
                notificationMessage.getTitle(),
                notificationMessage.getMessage(),
                notificationMessage.getPriority()
        );
        return ResponseEntity.ok().build();
    }

    /**
     * Enhanced notification sending with better logging
     */
    private void sendDepartmentNotification(String fromDepartment, String toDepartment, String type,
                                            String title, String message, String priority) {
        NotificationMessage notification = new NotificationMessage(
                type, title, message, fromDepartment, toDepartment, priority
        );

        // Normalize department name for topic
        String normalizedDept = toDepartment.toLowerCase().trim();
        String destination = "/topic/department/" + normalizedDept;

        log.info("🎯 Sending notification to destination: {}", destination);
        log.info("📝 Notification details: {}", notification);

        try {
            messagingTemplate.convertAndSend(destination, notification);
            log.info("✅ Successfully sent notification to {}", destination);
        } catch (Exception e) {
            log.error("❌ Failed to send notification to {}: {}", destination, e.getMessage());
        }
    }
}