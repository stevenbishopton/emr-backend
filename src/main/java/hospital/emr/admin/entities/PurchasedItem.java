package hospital.emr.admin.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasedItem {
    private Long id;
    private String name;
    private String quantity;
    private String rate;
    private String amount;
}
