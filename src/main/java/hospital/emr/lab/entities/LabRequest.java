package hospital.emr.lab.entities;

import hospital.emr.common.entities.Note;
import hospital.emr.lab.enums.LabPriority;
import hospital.emr.patient.entities.MedicalHistory;
import hospital.emr.patient.entities.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class LabRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medicalHistory_id")
    private MedicalHistory medicalHistory;

    private String orderedBy;

    @Enumerated(EnumType.STRING)
    private LabPriority priority;

    @ManyToMany
    @JoinTable(
            name = "lab_request_subtests",
            joinColumns = @JoinColumn(name = "lab_request_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_test_id")
    )
    private List<SubTest> requestedSubTests;

    private LocalDateTime timeIssued;


}