package hospital.emr.patient.entities;

import hospital.emr.bill.entities.Bill;
import hospital.emr.common.entities.NextOfKin;
import hospital.emr.common.enums.Sex;
import hospital.emr.reception.entities.Visit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "patients",
    indexes = {
        @Index(name = "idx_patient_phone", columnList = "phone_number"),
        @Index(name = "idx_patient_email", columnList = "email"),
        @Index(name = "idx_patient_names", columnList = "names")
    }
)
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "patient_code", nullable = false, unique = true, updatable = false)
    private String code;

    @Column(nullable = true)
    private String image;

    @Column(name = "names", nullable = false)
    private String names;

    @Column(name = "sex", nullable = false)
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "names", column = @Column(name = "next_of_kin_names")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "next_of_kin_phone_number")),
            @AttributeOverride(name = "relationship", column = @Column(name = "next_of_kin_relationship"))
    })
    private NextOfKin nextOfKin;

    @Column(name = "email", nullable = true, unique = true)
    private String email;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "occupation", nullable = true)
    private String occupation;

    @OneToMany
    private List<Bill> bills;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private MedicalHistory medicalHistory;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Visit> visits;

}

