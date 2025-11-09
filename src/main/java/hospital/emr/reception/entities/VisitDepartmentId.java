package hospital.emr.reception.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;


@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class VisitDepartmentId implements Serializable {
    private Long visitId;
    private Long departmentId;

}
