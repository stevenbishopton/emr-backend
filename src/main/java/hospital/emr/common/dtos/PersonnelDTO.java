package hospital.emr.common.dtos;

import hospital.emr.common.enums.PersonnelType;
import hospital.emr.common.enums.Sex;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PersonnelDTO {

    private Long id;

    @NotNull
    private String names;

    private String image;

    @NotNull
    private Sex sex;

    @NotNull
    private String phoneNumber;

    private String email;

    private String address;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private PersonnelType personnelType; // e.g., "DOCTOR", "NURSE", "ADMIN"

    private Long departmentId;
}
