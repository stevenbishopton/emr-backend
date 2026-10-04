package hospital.emr.lab.dtos;

import hospital.emr.lab.enums.SampleType;
import hospital.emr.lab.enums.SpecimenStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabSpecimenDTO {
    private Long id;
    private String accessionNumber;
    private SampleType sampleType;
    private SpecimenStatus status;
    private String collectedByName;
    private LocalDateTime collectedAt;
    private String receivedByName;
    private LocalDateTime receivedAt;
    private String rejectedReason;
    private String notes;
}
