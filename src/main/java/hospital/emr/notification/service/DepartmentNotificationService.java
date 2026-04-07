// src/main/java/hospital/emr/notification/service/DepartmentNotificationService.java
package hospital.emr.notification.service;

import hospital.emr.notification.model.NotificationMessage;
import hospital.emr.notification.model.StoredNotification;
import hospital.emr.notification.repos.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    /**
     * Enhanced method to send and store department notification
     */
    @Transactional
    public void sendDepartmentNotification(String fromDepartment, String toDepartment, String type,
                                           String title, String message, String priority) {
        sendDepartmentNotification(fromDepartment, toDepartment, type, title, message, priority, null);
    }

    /**
     * Enhanced method to send and store department notification (with optional metadata JSON)
     */
    @Transactional
    public void sendDepartmentNotification(String fromDepartment, String toDepartment, String type,
                                           String title, String message, String priority,
                                           @Nullable String metadata) {

        NotificationMessage notification = new NotificationMessage(
                type, title, message, fromDepartment, toDepartment, priority
        );
        notification.setMetadata(metadata);

        // 1. Store notification in database
        StoredNotification storedNotification = StoredNotification.builder()
                .type(type)
                .title(title)
                .message(message)
                .fromDepartment(fromDepartment)
                .toDepartment(toDepartment)
                .priority(priority)
                .metadata(metadata)
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .isDelivered(false)
                .build();

        notificationRepository.save(storedNotification);
        log.info("💾 Stored notification for department {}: {}", toDepartment, storedNotification);

        // 2. Try to send real-time notification
        String destination = "/topic/department/" + toDepartment.toLowerCase();
        try {
            messagingTemplate.convertAndSend(destination, notification);
            storedNotification.setIsDelivered(true);
            storedNotification.setDeliveredAt(LocalDateTime.now());
            notificationRepository.save(storedNotification);
            log.info("📤 Real-time notification sent to {}: {}", destination, notification);
        } catch (Exception e) {
            log.warn("⚠️ Could not send real-time notification to {}: {}", destination, e.getMessage());
            // Notification remains stored but undelivered
        }
    }

    /**
     * Get undelivered notifications for a department (for when they come online)
     */
    public List<StoredNotification> getUndeliveredNotifications(String toDepartment) {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<StoredNotification> undelivered = notificationRepository
                .findUndeliveredSince(toDepartment, oneHourAgo);

        log.info("📨 Found {} undelivered notifications for department {}", undelivered.size(), toDepartment);
        return undelivered;
    }

    /**
     * Mark notifications as delivered when user comes online
     */
    @Transactional
    public void markNotificationsAsDelivered(List<Long> notificationIds) {
        List<StoredNotification> notifications = notificationRepository.findAllById(notificationIds);
        LocalDateTime now = LocalDateTime.now();

        notifications.forEach(notification -> {
            notification.setIsDelivered(true);
            notification.setDeliveredAt(now);
        });

        notificationRepository.saveAll(notifications);
        log.info("✅ Marked {} notifications as delivered", notifications.size());
    }

    /**
     * Get recent notifications for a department
     */
    public List<StoredNotification> getRecentNotifications(String toDepartment, int hours) {
        LocalDateTime since = LocalDateTime.now().minusHours(hours);
        return notificationRepository.findByToDepartmentAndTimestampAfter(toDepartment, since);
    }

    /**
     * Get unread count for a department
     */
    public long getUnreadCount(String toDepartment) {
        return notificationRepository.countByToDepartmentAndIsReadFalse(toDepartment);
    }

    // Your existing methods remain the same, but now they'll use the enhanced version
    public void notifyNewPatient(String fromDepartment, String toDepartment) {
        sendDepartmentNotification(fromDepartment, toDepartment, "NEW_PATIENT",
                "New Patient Arrival", "A new patient has arrived and is ready for processing.", "MEDIUM");
    }

    public void notifyPatientReady(String fromDepartment, String toDepartment) {
        sendDepartmentNotification(fromDepartment, toDepartment, "PATIENT_READY",
                "Patient Ready", "A patient is ready for their next step.", "MEDIUM");
    }

    public void notifyUrgentCase(String fromDepartment, String toDepartment) {
        sendDepartmentNotification(fromDepartment, toDepartment, "URGENT_CASE",
                "Urgent Case Alert", "An urgent case requires immediate attention.", "URGENT");
    }

    public void notifyTestResultsReady(String fromDepartment, String toDepartment) {
        sendDepartmentNotification(fromDepartment, toDepartment, "RESULTS_READY",
                "Test Results Ready", "Lab or radiology results are available.", "HIGH");
    }

    public void notifyEquipmentReady(String fromDepartment, String toDepartment) {
        sendDepartmentNotification(fromDepartment, toDepartment, "EQUIPMENT_READY",
                "Equipment Ready", "Requested equipment is now available.", "LOW");
    }
}