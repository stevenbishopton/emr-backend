package hospital.emr.lab.entities;

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
    private String requestedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private String comments;

}
