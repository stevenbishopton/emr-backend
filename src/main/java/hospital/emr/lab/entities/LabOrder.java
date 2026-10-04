package hospital.emr.lab.entities;

import hospital.emr.lab.enums.LabOrderStatus;
import hospital.emr.lab.enums.LabPriority;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lab_orders", indexes = {
        @Index(name = "idx_lab_order_patient", columnList = "patient_id"),
        @Index(name = "idx_lab_order_visit", columnList = "visit_id"),
        @Index(name = "idx_lab_order_status", columnList = "status"),
        @Index(name = "idx_lab_order_created", columnList = "created_at")
})
public class LabOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "visit_id")
    private Long visitId;

    @Column(name = "medical_history_id")
    private Long medicalHistoryId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private LabOrderStatus status = LabOrderStatus.ORDERED;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private LabPriority priority = LabPriority.ROUTINE;

    @Column(name = "clinical_notes", columnDefinition = "TEXT")
    private String clinicalNotes;

    @Column(name = "ordered_by_id")
    private Long orderedById;

    @Column(name = "ordered_by_name", length = 255)
    private String orderedByName;

    @Column(name = "critical_acknowledged", nullable = false)
    private Boolean criticalAcknowledged = false;

    @Column(name = "critical_acknowledged_by_id")
    private Long criticalAcknowledgedById;

    @Column(name = "critical_acknowledged_by_name", length = 255)
    private String criticalAcknowledgedByName;

    @Column(name = "critical_acknowledged_at")
    private LocalDateTime criticalAcknowledgedAt;

    @OneToMany(mappedBy = "labOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LabOrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "labOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LabSpecimen> specimens = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public void addItem(LabOrderItem item) {
        item.setLabOrder(this);
        this.items.add(item);
    }
}
