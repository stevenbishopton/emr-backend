package hospital.emr.lab.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Generic lab test used for tests created through the catalog UI. Existing
 * specialized test types keep their own subclasses, but new catalog entries
 * need a concrete entity to persist into the SINGLE_TABLE {@link LabTest}
 * hierarchy.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("GENERIC")
public class GenericLabTest extends LabTest {

    @Column(name = "reference_range", columnDefinition = "TEXT")
    private String referenceRange;
}
