package hospital.emr.patient.dtos;

import lombok.Data;

@Data
public class PrescriptionEntryDTO {
    private Long id;
    private Long prescriptionId;
    private Long itemId;
    private String itemName;
    private String dosage;
    private String route;
    private Integer durationDays;
}