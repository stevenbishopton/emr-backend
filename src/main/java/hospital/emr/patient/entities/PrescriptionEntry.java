package hospital.emr.patient.entities;

import hospital.emr.pharmacy.entities.Item;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    name = "prescription_entry",
    indexes = {
        @Index(name = "idx_prescriptionentry_item", columnList = "item_id"),
        @Index(name = "idx_prescriptionentry_prescription", columnList = "prescription_id"),
    }
)
@Getter
@Setter
public class PrescriptionEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private String dosage; // e.g., "500mg twice daily"
    private String route;        // oral, IV, etc.
    private int durationDays;

    @ManyToOne
    private Prescription prescription;
}