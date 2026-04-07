package hospital.emr.reception.dtos;

import hospital.emr.reception.enums.VisitStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueFilterRequest {
    private VisitStatus status;
    private LocalDate date;
    private Boolean todayOnly = false;
}