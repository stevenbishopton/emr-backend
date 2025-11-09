package hospital.emr.patient.dtos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdmissionPrescriptionEntryDTO {
    private Long id;
    private Long itemId;
    private String itemName;
    private String instructions;
    private Long admissionId;
    private Long prescriptionChartId;
    private List<PrescriptionAdministrationDTO> administrations;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}