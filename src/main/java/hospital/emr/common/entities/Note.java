package hospital.emr.common.entities;

import hospital.emr.common.enums.NoteType;
import hospital.emr.patient.entities.MedicalHistory;
import hospital.emr.reception.entities.Visit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "note",
        indexes = {
                @Index(name = "idx_note_medicalhistory", columnList = "medical_history_id"),
                @Index(name = "idx_note_visit", columnList = "visit_id"),
                @Index(name = "idx_note_createdAt", columnList = "createdAt")
        }
)

public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NoteType noteType;

    private String content;

    private String author;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_history_id", nullable = true)
    private MedicalHistory medicalHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = true)
    private Visit visit;
}