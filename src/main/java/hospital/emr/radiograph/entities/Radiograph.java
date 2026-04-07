package hospital.emr.radiograph.entities;

import hospital.emr.patient.entities.MedicalHistory;
import hospital.emr.radiograph.enums.RadiographType;
import hospital.emr.radiograph.enums.RadiographStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "radiograph")
public class Radiograph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long patientId;

    private Long visitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_history_id")
    private MedicalHistory medicalHistory;

    @Enumerated(EnumType.STRING)
    private RadiographType radiographType;

    @Column(name = "radiograph_name", nullable = false)
    private String radiographName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RadiographStatus status = RadiographStatus.REQUESTED;

    private String carriedOutBy;

    private String interpretation;

    private String comments;

    private String orderDate;

    private String resultDate;

    // New fields for better tracking
    private String scheduledDate;

    private String scheduledTime;

    @Column(columnDefinition = "TEXT")
    private String technicianNotes;

    @Column(columnDefinition = "TEXT")
    private String radiologistNotes;

    @Column(length = 500)
    private String reportUrl;

    @Column(length = 500)
    private String imageUrl;

    @Column(nullable = false)
    private Long requestedBy = 1L;

    @Column(nullable = false)
    private Long departmentId = 1L;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}