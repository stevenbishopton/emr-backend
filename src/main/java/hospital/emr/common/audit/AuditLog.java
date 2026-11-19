package hospital.emr.common.audit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Generic audit record for mutating API activity across the EMR.
 * Captures who did what, to which resource, and the outcome.
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "audit_log",
        indexes = {
                @Index(name = "idx_audit_actor", columnList = "actor_id"),
                @Index(name = "idx_audit_resource", columnList = "resource"),
                @Index(name = "idx_audit_created", columnList = "created_at")
        }
)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "actor_id")
    private Long actorId;

    @Column(name = "actor_name")
    private String actorName;

    @Column(length = 10)
    private String method;

    @Column(length = 30)
    private String action;

    @Column(length = 100)
    private String resource;

    @Column(length = 500)
    private String path;

    private Integer status;

    @Column(name = "ip_address", length = 60)
    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(name = "duration_ms")
    private Long durationMs;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
