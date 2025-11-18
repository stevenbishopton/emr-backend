
package hospital.emr.pharmacy.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PxItemDeducted {
    private Long itemId;
    private String itemName;
    private String quantityDeducted;
}