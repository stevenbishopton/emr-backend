package hospital.emr.common.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "scheduled_visitor")
public class ScheduledVisitor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "names", nullable = false)
    private String names;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "days_of_expectancy", nullable = false)
    private String daysOfExpectancy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheduled_visitors_list_id")
    private ScheduledVisitorsList scheduledVisitorsList;

    @CreationTimestamp
    private LocalDateTime createdAt;
}