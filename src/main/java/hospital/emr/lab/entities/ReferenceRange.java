package hospital.emr.lab.entities;

import hospital.emr.common.enums.Sex;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reference_range")
public class ReferenceRange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Double minValue;
    private Double maxValue;
    private String unit;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    private Integer minAge;
    private Integer maxAge;

    @ManyToOne
    @JoinColumn(name = "sub_test_id")
    private SubTest subTest;

}