package hospital.emr.patient.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class PrescriptionAdministrationDTO {
    private Long id;
    private Long prescriptionEntryId;
    private LocalDate administrationDate;
    private LocalTime administrationTime;
    private Boolean administered;
    private String notes;
    private String administeredBy;
    private LocalDateTime createdAt;
}