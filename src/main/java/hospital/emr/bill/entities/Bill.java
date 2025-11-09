package hospital.emr.bill.entities;

import hospital.emr.patient.entities.Patient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bills")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "sub_bills", joinColumns = @JoinColumn(name = "bill_id"))
    private List<SubBill> subBills;

    private LocalDate dateIssued;

    private BigDecimal totalAmount;

    private String note;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @PrePersist
    @PreUpdate
    public void calculateTotalAmount() {
        this.totalAmount = (subBills == null ? BigDecimal.ZERO :
                subBills.stream()
                        .map(SubBill::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}