package hospital.emr.lab.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Defines the analytes (with units and reference/critical ranges) that make up a
 * lab test. Used to prefill result entry and to compute flags reliably.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lab_analyte_templates", indexes = {
        @Index(name = "idx_lab_analyte_template_test", columnList = "test_id")
})
public class LabAnalyteTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "test_id", nullable = false)
    private Long testId;

    @NotBlank
    @Column(name = "analyte_name", nullable = false, length = 200)
    private String analyteName;

    @Column(name = "unit", length = 50)
    private String unit;

    @Column(name = "ref_low", precision = 18, scale = 4)
    private BigDecimal refLow;

    @Column(name = "ref_high", precision = 18, scale = 4)
    private BigDecimal refHigh;

    @Column(name = "critical_low", precision = 18, scale = 4)
    private BigDecimal criticalLow;

    @Column(name = "critical_high", precision = 18, scale = 4)
    private BigDecimal criticalHigh;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public LabAnalyteTemplate(Long testId, String analyteName, String unit,
                              BigDecimal refLow, BigDecimal refHigh,
                              BigDecimal criticalLow, BigDecimal criticalHigh,
                              int displayOrder) {
        this.testId = testId;
        this.analyteName = analyteName;
        this.unit = unit;
        this.refLow = refLow;
        this.refHigh = refHigh;
        this.criticalLow = criticalLow;
        this.criticalHigh = criticalHigh;
        this.displayOrder = displayOrder;
    }
}
