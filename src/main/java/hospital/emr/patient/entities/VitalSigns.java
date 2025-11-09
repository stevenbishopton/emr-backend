package hospital.emr.patient.entities;


import hospital.emr.reception.entities.Visit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "vital_signs",
        indexes = {
                @Index(name = "idx_vitalsigns_medicalhistory", columnList = "medical_history_id"),
                @Index(name = "idx_vitalsigns_visit", columnList = "visit_id"),
                @Index(name = "idx_vitalsigns_time", columnList = "timeTaken"),
                @Index(name = "idx_vitalsigns_author", columnList = "author")
        }
)

public class VitalSigns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private LocalDateTime timeTaken;
    // Core vital signs
    private Double temperature;               // °C
    private Integer pulseRate;                    // bpm
    private Integer respiratoryRate;          // breaths/min
    private Integer systolicBp;               // mmHg
    private Integer diastolicBp;              // mmHg
    private Integer oxygenSaturation;         // %
    // Additional common fields
    private Double bloodGlucose;              // mg/dL or mmol/L
    private Double weight;                    // kg
    private Double height;                    // cm
    private Double bmi;                       // kg/m²

    private String author;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    private MedicalHistory medicalHistory;

    @ManyToOne
    @JoinColumn(name = "visit_id", nullable = true)
    private Visit visit;

}
