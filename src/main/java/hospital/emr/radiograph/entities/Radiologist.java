package hospital.emr.radiograph.entities;

import hospital.emr.common.entities.Personnel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("RADIOLOGIST")
public class Radiologist extends Personnel {
}
