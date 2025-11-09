package hospital.emr.patient.entities;

import hospital.emr.common.entities.Note;
import hospital.emr.reception.entities.Visit;
import hospital.emr.ward.entities.Ward;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Admission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Note admissionRecord;

    @ManyToOne
    @JoinColumn(name = "ward_id")
    private Ward ward;

    @OneToMany
    private List<Note> notes;

    @OneToOne
    @JoinColumn(name = "prescription_chart_id")
    private PrescriptionChart prescriptionChart;


    @Column(nullable = false)
    private LocalDateTime admissionDate;

    @Column(nullable = true)
    private LocalDateTime dischargeDate;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    private MedicalHistory medicalHistory;

    @ManyToOne
    @JoinColumn(name = "visit_id", nullable = true)
    private Visit visit;

}
