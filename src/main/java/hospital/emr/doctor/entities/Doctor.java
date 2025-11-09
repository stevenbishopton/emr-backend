package hospital.emr.doctor.entities;

import hospital.emr.common.entities.Personnel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DOCTOR")
public class Doctor extends Personnel {
    // You can add fields specific to a Admin here if needed,
    // but the common fields are now inherited from Personnel.
}