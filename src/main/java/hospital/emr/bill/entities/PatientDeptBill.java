package hospital.emr.bill.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "patient_dept_bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDeptBill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private String patientId;

    @Column(name = "patient_names", nullable = false)
    private String patientNames;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "amount", nullable = false)
    private String amount;

    private Boolean isPaid;

    private Boolean isAdmitted;

    private Long visitId;

    @CreationTimestamp
    @Column(name = "time_issued", updatable = false)
    private LocalDateTime timeIssued;

    @Column(name = "issuer", nullable = false)
    private String issuer;

    @Column(name = "issued_to", nullable = false)
    private String issuedTo;
}