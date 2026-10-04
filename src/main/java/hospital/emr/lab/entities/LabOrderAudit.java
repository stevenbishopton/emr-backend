package hospital.emr.lab.entities;

import hospital.emr.lab.enums.LabOrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lab_order_audit", indexes = {
        @Index(name = "idx_lab_audit_order", columnList = "order_id"),
        @Index(name = "idx_lab_audit_timestamp", columnList = "timestamp")
})
public class LabOrderAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "action", nullable = false, length = 50)
    private String action;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 30)
    private LabOrderStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", length = 30)
    private LabOrderStatus toStatus;

    @Column(name = "actor_id")
    private Long actorId;

    @Column(name = "actor_name", length = 255)
    private String actorName;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;
}
