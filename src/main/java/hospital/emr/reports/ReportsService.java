package hospital.emr.reports;

import hospital.emr.bill.entities.PatientDeptBill;
import hospital.emr.reception.entities.Visit;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor
public class ReportsService {

    private final EntityManager em;

    @Transactional(readOnly = true)
    public ReportsSummaryDTO getSummary(LocalDate from, LocalDate to) {
        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toDt = to.atTime(23, 59, 59);

        ReportsSummaryDTO dto = new ReportsSummaryDTO();
        dto.setFrom(from);
        dto.setTo(to);
        dto.setTotalPatients(count("SELECT COUNT(p) FROM Patient p"));
        dto.setVisitsInRange(countRange(
                "SELECT COUNT(v) FROM Visit v WHERE v.visitDateTime BETWEEN :f AND :t", fromDt, toDt));
        dto.setActiveAdmissions(count(
                "SELECT COUNT(a) FROM Admission a WHERE a.dischargeDate IS NULL"));
        dto.setLabOrdersInRange(countRange(
                "SELECT COUNT(o) FROM LabOrder o WHERE o.createdAt BETWEEN :f AND :t", fromDt, toDt));
        dto.setRadiographVisitsInRange(countRange(
                "SELECT COUNT(r) FROM RadiographVisitHistory r WHERE r.visitDate BETWEEN :f AND :t", fromDt, toDt));

        List<PatientDeptBill> bills = billsInRange(fromDt, toDt);
        double total = 0, paid = 0;
        for (PatientDeptBill bill : bills) {
            double amount = parseAmount(bill.getAmount());
            total += amount;
            if (Boolean.TRUE.equals(bill.getIsPaid())) {
                paid += amount;
            }
        }
        dto.setTotalBilled(total);
        dto.setPaidAmount(paid);
        dto.setOutstandingAmount(Math.max(0, total - paid));
        return dto;
    }

    @Transactional(readOnly = true)
    public List<DailyPointDTO> getDailySeries(LocalDate from, LocalDate to) {
        LocalDateTime fromDt = from.atStartOfDay();
        LocalDateTime toDt = to.atTime(23, 59, 59);

        Map<LocalDate, long[]> visitCounts = new TreeMap<>();
        List<Visit> visits = em.createQuery(
                        "SELECT v FROM Visit v WHERE v.visitDateTime BETWEEN :f AND :t", Visit.class)
                .setParameter("f", fromDt)
                .setParameter("t", toDt)
                .getResultList();
        for (Visit visit : visits) {
            if (visit.getVisitDateTime() == null) continue;
            LocalDate day = visit.getVisitDateTime().toLocalDate();
            visitCounts.computeIfAbsent(day, k -> new long[1])[0]++;
        }

        Map<LocalDate, double[]> revenue = new TreeMap<>();
        for (PatientDeptBill bill : billsInRange(fromDt, toDt)) {
            if (bill.getTimeIssued() == null) continue;
            LocalDate day = bill.getTimeIssued().toLocalDate();
            double[] slot = revenue.computeIfAbsent(day, k -> new double[2]);
            double amount = parseAmount(bill.getAmount());
            if (Boolean.TRUE.equals(bill.getIsPaid())) {
                slot[0] += amount;
            } else {
                slot[1] += amount;
            }
        }

        List<DailyPointDTO> points = new ArrayList<>();
        for (LocalDate day = from; !day.isAfter(to); day = day.plusDays(1)) {
            long count = visitCounts.containsKey(day) ? visitCounts.get(day)[0] : 0;
            double[] rev = revenue.containsKey(day) ? revenue.get(day) : new double[2];
            points.add(new DailyPointDTO(day, count, rev[0], rev[1]));
        }
        return points;
    }

    private List<PatientDeptBill> billsInRange(LocalDateTime from, LocalDateTime to) {
        return em.createQuery(
                        "SELECT b FROM PatientDeptBill b WHERE b.timeIssued BETWEEN :f AND :t",
                        PatientDeptBill.class)
                .setParameter("f", from)
                .setParameter("t", to)
                .getResultList();
    }

    private long count(String jpql) {
        return ((Number) em.createQuery(jpql).getSingleResult()).longValue();
    }

    private long countRange(String jpql, LocalDateTime from, LocalDateTime to) {
        return ((Number) em.createQuery(jpql)
                .setParameter("f", from)
                .setParameter("t", to)
                .getSingleResult()).longValue();
    }

    private double parseAmount(String amount) {
        if (amount == null || amount.isBlank()) return 0;
        try {
            return Double.parseDouble(amount);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
