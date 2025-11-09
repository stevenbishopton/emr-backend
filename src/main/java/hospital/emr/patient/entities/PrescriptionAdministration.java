package hospital.emr.patient.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "prescription_administration")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionAdministration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_entry_id")
    private AdmissionPrescriptionEntry prescriptionEntry;

    private LocalDate administrationDate;
    private LocalTime administrationTime;
    private Boolean administered;
    private String notes;
    private String administeredBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
}