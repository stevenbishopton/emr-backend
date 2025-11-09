package hospital.emr.lab.entities;

import hospital.emr.common.entities.Note;
import hospital.emr.patient.entities.MedicalHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lab_diagnostic_report")
public class LabDiagnosticReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    private MedicalHistory medicalHistory;

    @ManyToOne
    @JoinColumn(name = "lab_request_id")
    private LabRequest labRequest;

    @OneToMany(mappedBy = "labDiagnosticReport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LabTestResult> labTestResults;

    @OneToMany
    private List<Note> diagnosticNote;

    private String orderedBy;

    private String carriedOutBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}