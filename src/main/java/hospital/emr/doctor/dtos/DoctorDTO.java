package hospital.emr.doctor.dtos;

import hospital.emr.common.enums.Sex;
import lombok.Data;

@Data
public class DoctorDTO {
    private Long id;
    private String names;
    private Sex sex;
    private String image;
    private String phoneNumber;
    private String email;
    private String address;

}
