// src/main/java/hospital/emr/notification/model/StoredNotification.java
package hospital.emr.notification.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoredNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private String fromDepartment;

    @Column(nullable = false)
    private String toDepartment;

    private String toUserId; // Optional: for user-specific notifications

    @Column(nullable = false)
    private String priority;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Boolean isRead = false;

    @Column(nullable = false)
    private Boolean isDelivered = false;

    private LocalDateTime deliveredAt;

    // Additional metadata
    @Column(columnDefinition = "TEXT")
    private String metadata; // JSON string for additional data
}