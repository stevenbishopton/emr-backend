package hospital.emr.radiograph.entities;

import hospital.emr.radiograph.enums.RadiographStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "radiograph_history")
public class RadiographHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "radiograph_id", nullable = false)
    private Long radiographId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RadiographStatus status;

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
