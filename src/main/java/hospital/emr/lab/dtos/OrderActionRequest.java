package hospital.emr.lab.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderActionRequest {

    private String notes;

    private String reason;
}
