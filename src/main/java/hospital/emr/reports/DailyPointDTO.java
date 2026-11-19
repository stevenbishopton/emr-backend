package hospital.emr.reports;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyPointDTO {
    private LocalDate date;
    private long count;
    private double paid;
    private double unpaid;

    public DailyPointDTO(LocalDate date, long count, double paid, double unpaid) {
        this.date = date;
        this.count = count;
        this.paid = paid;
        this.unpaid = unpaid;
    }
}
