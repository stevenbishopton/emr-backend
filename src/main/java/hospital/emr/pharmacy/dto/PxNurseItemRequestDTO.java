package hospital.emr.pharmacy.dto;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PxNurseItemRequestDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private Long patientId;
    private Long visitId;
    private String patientNames;
    private String patientCode;
    private Long wardId;
    private String wardName;
    private String requester;
    private String content;
    @CreationTimestamp
    private LocalDateTime createdAt;

}
