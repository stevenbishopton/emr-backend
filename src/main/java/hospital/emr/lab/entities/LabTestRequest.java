package hospital.emr.lab.entities;

import hospital.emr.lab.enums.TestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LabTestRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private Long patientId;

    private Long visitId;

    private Long medicalHistoryId;

    @ElementCollection
    @CollectionTable(name = "lab_test_request_tests",
            joinColumns = @JoinColumn(name = "lab_test_request_id"))
    @Column(name = "lab_test_id")
    private List<Long> labTestIds = new ArrayList<>();
    
    @Column(length = 255)
    private String requestedBy;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TestStatus status = TestStatus.PENDING;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TEXT")
    private String comments;

}
