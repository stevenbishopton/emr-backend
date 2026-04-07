package hospital.emr.radiograph.entities;

import hospital.emr.patient.entities.Patient;
import hospital.emr.reception.entities.Visit;
import hospital.emr.common.entities.Personnel;
import hospital.emr.radiograph.enums.RadiographStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "radiograph_visit_history")
public class RadiographVisitHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id")
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private Personnel requestedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private Personnel performedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "radiologist_id")
    private Personnel radiologist;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RadiographStatus status;

    @Column(nullable = false)
    private String visitType; // e.g., "ROUTINE", "EMERGENCY", "FOLLOW_UP"

    @Column(columnDefinition = "TEXT")
    private String clinicalNotes;

    @Column(columnDefinition = "TEXT")
    private String radiologistReport;

    @Column(columnDefinition = "TEXT")
    private String technicianNotes;

    @Column(length = 500)
    private String reportUrl;

    @Column(length = 500)
    private String imageUrl;

    @Column(name = "visit_date", nullable = false)
    private LocalDateTime visitDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    // List of radiograph tests performed during this visit
    @OneToMany(mappedBy = "visitHistory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RadiographVisitTest> testsPerformed;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
