package hospital.emr.lab.entities;

import hospital.emr.lab.enums.TestStatus;
import hospital.emr.patient.entities.MedicalHistory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity to store laboratory test results for patients
 * Stores results as manually typed text with reference to test IDs
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lab_test_results", indexes = {
    @Index(name = "idx_lab_result_patient", columnList = "patient_id"),
    @Index(name = "idx_lab_result_visit", columnList = "visit_id"),
    @Index(name = "idx_lab_result_status", columnList = "status"),
    @Index(name = "idx_lab_result_created", columnList = "created_at")
})
public class LabTestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull(message = "Patient ID is required")
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "visit_id")
    private Long visitId;

    private String carriedOutBy;

    /**
     * List of test IDs that were performed
     * Stored as comma-separated string in database
     */
    @ElementCollection
    @CollectionTable(name = "lab_test_result_test_ids", joinColumns = @JoinColumn(name = "result_id"))
    @Column(name = "test_id")
    private List<Long> testIds = new ArrayList<>();

    /**
     * Manually typed results by lab technician
     */
    @Column(name = "results", columnDefinition = "TEXT")
    private String results;

    /**
     * Interpretation of the results
     */
    @Column(name = "interpretation", columnDefinition = "TEXT")
    private String interpretation;

    /**
     * Additional comments
     */
    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private TestStatus status = TestStatus.PENDING;

    /**
     * Lab technician who performed the test
     */
    private String requestedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * Reference to the medical history this lab test result belongs to
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_history_id")
    private MedicalHistory medicalHistory;

}
