package hospital.emr.reports;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportsSummaryDTO {
    private LocalDate from;
    private LocalDate to;
    private long totalPatients;
    private long visitsInRange;
    private long activeAdmissions;
    private long labOrdersInRange;
    private long radiographVisitsInRange;
    private double totalBilled;
    private double paidAmount;
    private double outstandingAmount;
}
