package hospital.emr.lab.entities;

import hospital.emr.lab.enums.ResultFlag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lab_analyte_results", indexes = {
        @Index(name = "idx_lab_analyte_item", columnList = "order_item_id"),
        @Index(name = "idx_lab_analyte_flag", columnList = "flag")
})
public class LabAnalyteResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private LabOrderItem orderItem;

    @NotBlank
    @Column(name = "analyte_name", nullable = false, length = 200)
    private String analyteName;

    @Column(name = "value_numeric", precision = 18, scale = 4)
    private BigDecimal valueNumeric;

    @Column(name = "value_text", columnDefinition = "TEXT")
    private String valueText;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "flag", nullable = false, length = 20)
    private ResultFlag flag = ResultFlag.UNKNOWN;

    @Column(name = "method", length = 100)
    private String method;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
