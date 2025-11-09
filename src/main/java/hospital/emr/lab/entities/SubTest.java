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
@NoArgsConstructor
@Table(name = "sub_test")
public class SubTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ClinicalTestType clinicalTestType;

    private String name;

    private String resultValue;

    @OneToMany(mappedBy = "subTest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReferenceRange> referenceRanges;

    @ManyToOne
    @JoinColumn(name = "lab_test_result_id")
    private LabTestResult labTestResult;
}