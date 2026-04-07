package hospital.emr.lab.entities;

import hospital.emr.lab.enums.SampleType;
import hospital.emr.lab.enums.TestCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Base abstract entity for all laboratory tests using SINGLE_TABLE inheritance strategy.
 * All 53 specific test types will extend this class.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "test_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "lab_tests")
public abstract class LabTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Test name is required")
    @Column(name = "name", nullable = false, unique = true, length = 200)
    private String name;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Category is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private TestCategory category;

    @NotNull(message = "Sample type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "sample_type", nullable = false, length = 20)
    private SampleType sampleType;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive =  true;

}
