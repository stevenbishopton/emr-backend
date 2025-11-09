package hospital.emr.pharmacy.entities;

import hospital.emr.common.entities.Personnel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("PHARMACIST")
public class Pharmacist extends Personnel {
    // You can add fields specific to a Admin here if needed,
    // but the common fields are now inherited from Personnel.
}