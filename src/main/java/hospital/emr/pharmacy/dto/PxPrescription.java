package hospital.emr.pharmacy.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Embeddable
public class PxPrescription {
    private String itemId;
    private String itemName;
    private String quantity;
    private BigDecimal totalAmount;
}
