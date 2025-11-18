// PxNurseDeduction.java (Entity)
package hospital.emr.pharmacy.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "px_nurse_deduction")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PxNurseDeduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemRequestId;

    private String dispenser;

    @ElementCollection
    @CollectionTable(name = "px_deducted_items", joinColumns = @JoinColumn(name = "deduction_id"))
    private List<PxItemDeducted> itemDeductedList;

    @CreationTimestamp
    private LocalDateTime createdAt;
}