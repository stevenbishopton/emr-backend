package hospital.emr.bill.entities;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Embeddable
public class SubBill {
    private String category;
    private BigDecimal amount;
}