package hospital.emr.ward.dtos;

import hospital.emr.patient.dtos.AdmissionDTO;
import lombok.Data;

import java.util.List;

@Data
public class WardDTO {
    private Long id;
    private String name;
    private List<AdmissionDTO> admissions;
}
