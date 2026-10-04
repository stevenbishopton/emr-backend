package hospital.emr.lab.entities;

import hospital.emr.lab.enums.LabOrderStatus;
import hospital.emr.lab.enums.SampleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "lab_order_items", indexes = {
        @Index(name = "idx_lab_order_item_order", columnList = "order_id"),
        @Index(name = "idx_lab_order_item_test", columnList = "test_id"),
        @Index(name = "idx_lab_order_item_status", columnList = "status")
})
public class LabOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private LabOrder labOrder;

    @NotNull
    @Column(name = "test_id", nullable = false)
    private Long testId;

    @Column(name = "test_name", nullable = false, length = 200)
    private String testName;

    @Enumerated(EnumType.STRING)
    @Column(name = "sample_type", length = 20)
    private SampleType sampleType;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private LabOrderStatus status = LabOrderStatus.ORDERED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specimen_id")
    private LabSpecimen specimen;

    @Column(name = "performed_by_id")
    private Long performedById;

    @Column(name = "performed_by_name", length = 255)
    private String performedByName;

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    @Column(name = "resulted_by_id")
    private Long resultedById;

    @Column(name = "resulted_by_name", length = 255)
    private String resultedByName;

    @Column(name = "resulted_at")
    private LocalDateTime resultedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LabAnalyteResult> analyteResults = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    public void addAnalyteResult(LabAnalyteResult result) {
        result.setOrderItem(this);
        this.analyteResults.add(result);
    }
}
