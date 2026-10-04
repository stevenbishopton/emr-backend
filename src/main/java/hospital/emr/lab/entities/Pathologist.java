package hospital.emr.lab.entities;

import hospital.emr.common.entities.Personnel;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Pathologist / consultant responsible for verifying and releasing lab results.
 * Gives the {@code ROLE_PATHOLOGIST} authority.
 */
@Entity
@DiscriminatorValue("PATHOLOGIST")
public class Pathologist extends Personnel {
}
