package hospital.emr.patient.dtos;

import lombok.Data;

@Data
public class PrescriptionCellUpdateDTO {
    private Long entryId;
    private String fieldName; // "itemName", "instructions", "administration.2024-01-15.08:00", etc.
    private String value;
}