package hospital.emr.patient.dtos;

import hospital.emr.bill.dtos.BillDTO;
import hospital.emr.common.enums.Sex;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PatientDTO {
    private Long id;
    private String code;
    private String image;
    private String names;
    private Sex sex;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private String address;
    private List<BillDTO> bills;
    private String occupation;
    private Long medicalHistoryId;
    private NextOfKinDTO nextOfKin;
    
    @Data
    public static class NextOfKinDTO {
        private String names;
        private String phoneNumber;
        private String relationship;
    }
}
