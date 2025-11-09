package hospital.emr.lab.entities;

import hospital.emr.lab.enums.ClinicalTestType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "lab_test_results")
@NoArgsConstructor
public class LabTestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ClinicalTestType testType;

    @OneToMany(mappedBy = "labTestResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTest> subTests;

    @ManyToOne
    @JoinColumn(name = "lab_report_id")
    private LabDiagnosticReport labDiagnosticReport;

}