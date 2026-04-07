package hospital.emr.nurse.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "nursing_reports")
public class NursingReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private LocalDateTime createdAt;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;

}