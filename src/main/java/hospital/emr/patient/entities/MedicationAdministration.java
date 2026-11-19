package hospital.emr.patient.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A dose administered against a prescribed medication ({@link PrescriptionEntry}).
 * This replaces the old admission-chart administrations so that the drug list the
 * doctor prescribes is the exact list nurses record doses against.
 */
@Entity
@Table(
        name = "medication_administration",
        indexes = {
                @Index(name = "idx_medadmin_entry", columnList = "prescription_entry_id"),
                @Index(name = "idx_medadmin_date", columnList = "administration_date")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicationAdministration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prescription_entry_id", nullable = false)
    private PrescriptionEntry prescriptionEntry;

    private LocalDate administrationDate;
    private LocalTime administrationTime;
    private Boolean administered;
    private String notes;
    private String administeredBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
