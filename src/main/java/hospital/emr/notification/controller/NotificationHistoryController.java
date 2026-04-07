// src/main/java/hospital/emr/notification/controller/NotificationHistoryController.java
package hospital.emr.notification.controller;

import hospital.emr.notification.model.StoredNotification;
import hospital.emr.notification.service.DepartmentNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationHistoryController {

    private final DepartmentNotificationService notificationService;

    /**
     * Get missed notifications when user comes online
     */
    @GetMapping("/department/{departmentId}/missed")
    public ResponseEntity<List<StoredNotification>> getMissedNotifications(
            @PathVariable String departmentId) {

        log.info("📨 Fetching missed notifications for department: {}", departmentId);
        List<StoredNotification> missed = notificationService.getUndeliveredNotifications(departmentId);

        // Mark them as delivered
        List<Long> deliveredIds = missed.stream()
                .map(StoredNotification::getId)
                .toList();
        notificationService.markNotificationsAsDelivered(deliveredIds);

        return ResponseEntity.ok(missed);
    }

    /**
     * Get recent notifications (last 24 hours)
     */
    @GetMapping("/department/{departmentId}/recent")
    public ResponseEntity<List<StoredNotification>> getRecentNotifications(
            @PathVariable String departmentId,
            @RequestParam(defaultValue = "24") int hours) {

        List<StoredNotification> recent = notificationService.getRecentNotifications(departmentId, hours);
        return ResponseEntity.ok(recent);
    }

    /**
     * Get unread count
     */
    @GetMapping("/department/{departmentId}/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @PathVariable String departmentId) {

        long count = notificationService.getUnreadCount(departmentId);
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    /**
     * Mark notification as read
     */
    @PostMapping("/{notificationId}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        // Implementation to mark as read
        return ResponseEntity.ok().build();
    }

    /**
     * Mark all department notifications as read
     */
    @PostMapping("/department/{departmentId}/read-all")
    public ResponseEntity<Void> markAllAsRead(@PathVariable String departmentId) {
        // Implementation to mark all as read
        return ResponseEntity.ok().build();
    }
}