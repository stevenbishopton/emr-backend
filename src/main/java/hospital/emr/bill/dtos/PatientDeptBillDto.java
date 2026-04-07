package hospital.emr.bill.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDeptBillDto {
    private Long id;

    @NotBlank(message = "Patient ID is required")
    private String patientId;

    @NotBlank(message = "Patient names are required")
    private String patientNames;

    private String purpose;

    private Long visitId;

    @NotBlank(message = "Amount is required")
    private String amount;

    private Boolean isPaid;

    private Boolean isAdmitted;

    private LocalDateTime timeIssued;

    @NotBlank(message = "Issuer is required")
    private String issuer;

    @NotBlank(message = "Issued to is required")
    private String issuedTo;
}