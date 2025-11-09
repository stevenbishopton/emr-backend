package hospital.emr.reception.entities;

import hospital.emr.patient.entities.Patient;
import hospital.emr.reception.enums.VisitStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visit")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreationTimestamp
    private LocalDateTime visitDateTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @NotNull
    private Patient patient;

    // This defines the one-to-many relationship from Visit to the linking table.
    // The "mappedBy" attribute indicates that the relationship is managed by the "visit" field in the VisitDepartment class.
    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VisitDepartment> visitDepartments;
}
