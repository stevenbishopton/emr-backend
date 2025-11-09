package hospital.emr.common.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "scheduled_visitors")
public class ScheduledVisitorsList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "department", nullable = false)
    private String department;

    @OneToMany(mappedBy = "scheduledVisitorsList", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ScheduledVisitor> scheduledVisitors;
}