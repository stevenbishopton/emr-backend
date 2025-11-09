package hospital.emr.pharmacy.entities;

import hospital.emr.pharmacy.enums.ItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "items",
    indexes = {
        @Index(name = "idx_item_name", columnList = "name"),
        @Index(name = "idx_item_type", columnList = "item_type"),
        @Index(name = "idx_item_expiration", columnList = "expiration_date")
    }
)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_type", nullable = false)
    private ItemType itemType;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "expiration_date", nullable = true)
    private LocalDate expirationDate;

    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;

    @Column(name = "cost_price", nullable = true)
    private BigDecimal costPrice;

    @Column(name = "selling_price", nullable = false)
    private BigDecimal sellingPrice;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "link", nullable = true)
    private String link;



}
