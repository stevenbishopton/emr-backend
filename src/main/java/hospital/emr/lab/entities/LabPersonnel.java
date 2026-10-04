package hospital.emr.lab.entities;

import hospital.emr.common.entities.Personnel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("LAB_PERSONNEL")
public class LabPersonnel extends Personnel {
    // You can add fields specific to an Admin here if needed,
    // but the common fields are now inherited from Personnel.
}