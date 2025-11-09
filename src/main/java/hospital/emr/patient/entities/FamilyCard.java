package hospital.emr.patient.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
//im thinking of the frontend process just being creation of a family card and then add patients to
// this umbrella
//
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "family_card",
    indexes = {
        @Index(name = "idx_familycard_names", columnList = "names")
    }
)
public class FamilyCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "names", nullable = false)
    private String names;

    @OneToMany
    private List<Patient> members;

}