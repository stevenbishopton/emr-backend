package hospital.emr.lab.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnterResultsRequest {

    @NotEmpty(message = "At least one item result is required")
    @Valid
    private List<ItemResult> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemResult {
        @NotNull(message = "Order item ID is required")
        private Long orderItemId;
        private String notes;
        private List<AnalyteInput> analytes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnalyteInput {
        private String analyteName;
        private BigDecimal valueNumeric;
        private String valueText;
        private String unit;
        private BigDecimal refLow;
        private BigDecimal refHigh;
        private BigDecimal criticalLow;
        private BigDecimal criticalHigh;
        private String method;
        private String comment;
    }
}
