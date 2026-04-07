package hospital.emr.radiograph.entities;

import hospital.emr.radiograph.entities.RadiographCatalog;
import hospital.emr.radiograph.enums.RadiographType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "radiograph_visit_test")
public class RadiographVisitTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_history_id", nullable = false)
    private RadiographVisitHistory visitHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_item_id")
    private RadiographCatalog catalogItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RadiographType type;

    @Column(nullable = false)
    private String testName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String status; // e.g., "PENDING", "IN_PROGRESS", "COMPLETED", "CANCELLED"

    @Column(columnDefinition = "TEXT")
    private String findings;

    @Column(columnDefinition = "TEXT")
    private String impression;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 500)
    private String reportUrl;

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    @Column(name = "reported_at")
    private LocalDateTime reportedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;
}
