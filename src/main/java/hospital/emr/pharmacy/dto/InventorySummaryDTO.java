package hospital.emr.pharmacy.dto;

import lombok.Data;

import java.math.BigDecimal;

/** Aggregate inventory health metrics for the pharmacy dashboard. */
@Data
public class InventorySummaryDTO {
    private long totalItems;
    private long expiredItems;
    private long expiringSoonItems;
    private long lowStockItems;
    private long outOfStockItems;
    private BigDecimal totalStockValue;
    private int expiringWindowDays;
    private BigDecimal lowStockThreshold;
}
