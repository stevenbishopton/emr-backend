package hospital.emr.pharmacy.entities;

import hospital.emr.pharmacy.dto.PxPrescription;
import hospital.emr.pharmacy.enums.PxPatientBillStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "px_patient_bill")
@AllArgsConstructor
@NoArgsConstructor
public class PxPatientBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "px_patient_bill_prescriptions", joinColumns = @JoinColumn(name = "bill_id"))
    private List<PxPrescription> prescriptions;


    private String totalAmount;

    private Long dispenserId;

    private String dispenserName;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    private String patientName;

    @Enumerated(EnumType.STRING)
    private PxPatientBillStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

}