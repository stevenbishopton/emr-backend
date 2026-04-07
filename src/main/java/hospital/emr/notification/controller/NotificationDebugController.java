package hospital.emr.notification.controller;

import hospital.emr.notification.model.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationDebugController {

    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/test-to-doctor")
    public ResponseEntity<Map<String, String>> testDoctorNotification() {
        Map<String, String> response = new HashMap<>();

        try {
            // Test sending to common doctor department names
            String[] doctorDepartments = {"emergency", "doctor", "doctors", "medical", "er", "emergency-room"};

            for (String dept : doctorDepartments) {
                String destination = "/topic/department/" + dept;
                NotificationMessage message = new NotificationMessage(
                        "NEW_PATIENT",
                        "Test Patient Arrival",
                        "Test patient John Doe has been sent to doctor queue",
                        "reception",
                        dept,
                        "HIGH"
                );

                messagingTemplate.convertAndSend(destination, message);
                log.info("✅ Test notification sent to: {}", destination);
                response.put(dept, "Notification sent to " + destination);
            }

            response.put("status", "success");
            response.put("message", "Test notifications sent to all doctor departments");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Error sending test notifications: {}", e.getMessage());
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/check-subscriptions")
    public ResponseEntity<Map<String, Object>> checkSubscriptions() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.LocalDateTime.now());
        response.put("availableEndpoints", new String[]{
                "/topic/department/emergency",
                "/topic/department/doctor",
                "/topic/department/doctors",
                "/topic/department/medical",
                "/topic/department/er",
                "/topic/department/reception",
                "/topic/department/admin"
        });
        response.put("message", "Use POST /api/debug/notifications/test-to-doctor to test notifications");
        return ResponseEntity.ok(response);
    }
}