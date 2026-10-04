package hospital.emr.lab.entities;

import hospital.emr.lab.enums.SampleType;
import hospital.emr.lab.enums.SpecimenStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lab_specimens", indexes = {
        @Index(name = "idx_lab_specimen_order", columnList = "order_id"),
        @Index(name = "idx_lab_specimen_accession", columnList = "accession_number", unique = true)
})
public class LabSpecimen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private LabOrder labOrder;

    @Column(name = "accession_number", nullable = false, unique = true, length = 64)
    private String accessionNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sample_type", nullable = false, length = 20)
    private SampleType sampleType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SpecimenStatus status = SpecimenStatus.PENDING;

    @Column(name = "collected_by_id")
    private Long collectedById;

    @Column(name = "collected_by_name", length = 255)
    private String collectedByName;

    @Column(name = "collected_at")
    private LocalDateTime collectedAt;

    @Column(name = "received_by_id")
    private Long receivedById;

    @Column(name = "received_by_name", length = 255)
    private String receivedByName;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "rejected_reason", columnDefinition = "TEXT")
    private String rejectedReason;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
