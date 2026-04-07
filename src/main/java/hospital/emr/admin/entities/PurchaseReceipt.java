package hospital.emr.admin.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "purchase_receipt")
public class PurchaseReceipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String clientName;

    private String orderedBy;

    @ElementCollection
    private List<PurchasedItem> purchasedItems;

    private String totalAmount;

    private String notes;

    private LocalDate dateOfArrival;


}