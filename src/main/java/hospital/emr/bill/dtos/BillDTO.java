package hospital.emr.bill.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class BillDTO {
    private Long id;
    private Long patientId;
    private String patientNames;
    private BigDecimal totalAmount;
    private String note;
    private LocalDate dateIssued;
    private List<SubBillDTO> subBills;
}