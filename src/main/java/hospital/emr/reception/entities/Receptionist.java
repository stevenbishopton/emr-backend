package hospital.emr.reception.entities;

import hospital.emr.common.entities.Personnel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;



@Entity
@DiscriminatorValue("RECEPTIONIST")
public class Receptionist extends Personnel {
    // You can add fields specific to a Admin here if needed,
    // but the common fields are now inherited from Personnel.
}