package hospital.emr.patient.entities;

import hospital.emr.reception.entities.Visit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(
    name = "prescription",
    indexes = {
        @Index(name = "idx_prescription_medicalhistory", columnList = "medical_history_id"),
        @Index(name = "idx_prescription_created", columnList = "createdAt")
    }
)
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescriptionEntry> prescriptionEntries;

    private String additionalInstructions;

    private Boolean isDischarge;

    private String prescriberName;

    @ManyToOne
    @JoinColumn(name = "visit_id", nullable = true)
    private Visit visit;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    private MedicalHistory medicalHistory;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
