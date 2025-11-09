package hospital.emr.patient.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VitalSignsDTO {
    private Long id;
    private Long visitId;
    private LocalDateTime timeTaken;
    // Core vital signs
    private Double temperature;               // °C
    private Integer pulseRate;                    // bpm
    private Integer respiratoryRate;          // breaths/min
    private Integer systolicBp;               // mmHg
    private Integer diastolicBp;              // mmHg
    private Integer oxygenSaturation;         // %
    // Additional common fields
    private Double bloodGlucose;              // mg/dL or mmol/L
    private Double weight;                    // kg
    private Double height;                    // cm
    private Double bmi;                       // kg/m²
    private String author;
    private Long medicalHistoryId;
}
