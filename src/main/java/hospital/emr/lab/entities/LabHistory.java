package hospital.emr.lab.entities;

import hospital.emr.lab.enums.TestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "lab_history")
public class LabHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lab_test_request_id")
    private Long labTestRequestId;

    @Column(name = "lab_test_result_id")
    private Long labTestResultId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TestStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private Long performedBy;

    @CreationTimestamp
    private LocalDateTime timestamp;

    private String previousStatus;

    @Column(length = 255)
    private String reason;

    // Additional fields for better tracking
    private String performedByName;
    
    @Column(length = 500)
    private String departmentName;
}
