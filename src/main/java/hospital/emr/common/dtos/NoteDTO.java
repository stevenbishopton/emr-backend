package hospital.emr.common.dtos;

import hospital.emr.common.enums.NoteType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteDTO {
    private Long id;
    private NoteType noteType;
    private String content;
    private String author;
    private LocalDateTime createdAt;
    private Long medicalHistoryId;
    private Long visitId;
}
