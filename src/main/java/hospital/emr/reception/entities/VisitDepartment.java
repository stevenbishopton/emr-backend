package hospital.emr.reception.entities;

import hospital.emr.common.entities.Department;
import hospital.emr.reception.enums.VisitStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "visit_department",
        indexes = {
                @Index(name = "idx_vd_department_assigned", columnList = "department_id, assigned_at"),
                @Index(name = "idx_vd_visit", columnList = "visit_id")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VisitDepartment {

    @EmbeddedId
    private VisitDepartmentId id;

    @ManyToOne
    @MapsId("visitId")
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    @ManyToOne
    @MapsId("departmentId")
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // When visit was queued for this department
    @Column(name = "assigned_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime assignedAt;

    // Who handled it (store personnel id to avoid cyclic dependencies; or a proper ManyToOne to Personnel)
    @Column(name = "handled_by_personnel_id")
    private Long handledByPersonnelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private VisitStatus status;

    @Column(name = "notes")
    private String notes;

    // Optimistic locking to prevent concurrent pickup issues
    @Version
    @Column(name = "version")
    private Long version;

}
