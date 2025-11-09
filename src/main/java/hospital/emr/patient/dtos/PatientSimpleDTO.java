package hospital.emr.patient.dtos;

import hospital.emr.common.enums.Sex;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientSimpleDTO {
    private Long id;
    private String code;
    private String names;
    private Sex sex;
    private LocalDate dateOfBirth;
}
