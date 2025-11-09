package hospital.emr.admin.entities;

import hospital.emr.common.entities.Personnel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Personnel {
    // You can add fields specific to a Admin here if needed,
    // but the common fields are now inherited from Personnel.
}