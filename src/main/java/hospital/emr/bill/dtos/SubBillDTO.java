package hospital.emr.bill.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubBillDTO {
    private String category;
    private BigDecimal amount;
}
