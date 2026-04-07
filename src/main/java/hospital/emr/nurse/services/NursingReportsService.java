package hospital.emr.nurse.services;

import hospital.emr.nurse.entities.NursingReports;
import hospital.emr.nurse.repos.NursingReportsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NursingReportsService {

    private final NursingReportsRepository repository;

    // Create a new report
    public NursingReports createReport(NursingReports report) {
        try {
            // Set creation timestamp if not set
            if (report.getCreatedAt() == null) {
                report.setCreatedAt(LocalDateTime.now());
            }

            NursingReports savedReport = repository.save(report);
            log.info("Created nursing report with ID: {}, Subject: {}", savedReport.getId(), savedReport.getSubject());
            return savedReport;
        } catch (Exception e) {
            log.error("Error creating nursing report: {}", e.getMessage());
            throw new RuntimeException("Failed to create nursing report: " + e.getMessage());
        }
    }

    // Get report by ID
    @Transactional(readOnly = true)
    public NursingReports getReportById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nursing report not found with ID: " + id));
    }

    // Get all reports
    @Transactional(readOnly = true)
    public List<NursingReports> getAllReports() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error fetching all reports: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // Get recent reports
    @Transactional(readOnly = true)
    public List<NursingReports> getRecentReports() {
        return repository.findTop10ByOrderByCreatedAtDesc();
    }

    // Update report
    public NursingReports updateReport(Long id, NursingReports report) {
        try {
            NursingReports existingReport = getReportById(id);

            // Update fields if provided
            if (report.getSubject() != null) {
                existingReport.setSubject(report.getSubject());
            }
            if (report.getContent() != null) {
                existingReport.setContent(report.getContent());
            }
            if (report.getAuthor() != null) {
                existingReport.setAuthor(report.getAuthor());
            }

            NursingReports updatedReport = repository.save(existingReport);
            log.info("Updated nursing report with ID: {}", id);
            return updatedReport;
        } catch (Exception e) {
            log.error("Error updating nursing report with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to update nursing report: " + e.getMessage());
        }
    }

    // Delete report
    public void deleteReport(Long id) {
        try {
            if (!repository.existsById(id)) {
                throw new RuntimeException("Nursing report not found with ID: " + id);
            }

            repository.deleteById(id);
            log.info("Deleted nursing report with ID: {}", id);
        } catch (Exception e) {
            log.error("Error deleting nursing report with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Failed to delete nursing report: " + e.getMessage());
        }
    }

    // Search reports with multiple criteria
    @Transactional(readOnly = true)
    public List<NursingReports> searchReports(String author, String subject, String content,
                                              LocalDateTime startDate, LocalDateTime endDate) {
        List<NursingReports> allReports = repository.findAll();
        List<NursingReports> results = new ArrayList<>();

        for (NursingReports report : allReports) {
            boolean matches = true;

            // Filter by author
            if (author != null && !author.trim().isEmpty()) {
                String reportAuthor = report.getAuthor();
                if (reportAuthor == null || !reportAuthor.toLowerCase().contains(author.trim().toLowerCase())) {
                    matches = false;
                }
            }

            // Filter by subject
            if (subject != null && !subject.trim().isEmpty()) {
                String reportSubject = report.getSubject();
                if (reportSubject == null || !reportSubject.toLowerCase().contains(subject.trim().toLowerCase())) {
                    matches = false;
                }
            }

            // Filter by content
            if (content != null && !content.trim().isEmpty()) {
                String reportContent = report.getContent();
                if (reportContent == null || !reportContent.toLowerCase().contains(content.trim().toLowerCase())) {
                    matches = false;
                }
            }

            // Filter by date range
            if (startDate != null && report.getCreatedAt() != null) {
                if (report.getCreatedAt().isBefore(startDate)) {
                    matches = false;
                }
            }

            if (endDate != null && report.getCreatedAt() != null) {
                // Set end date to end of day
                LocalDateTime endOfDay = endDate.withHour(23).withMinute(59).withSecond(59);
                if (report.getCreatedAt().isAfter(endOfDay)) {
                    matches = false;
                }
            }

            if (matches) {
                results.add(report);
            }
        }

        return results;
    }

    // Get reports by author
    @Transactional(readOnly = true)
    public List<NursingReports> getReportsByAuthor(String author) {
        return repository.findByAuthor(author);
    }

    // Search by subject
    @Transactional(readOnly = true)
    public List<NursingReports> searchBySubject(String subject) {
        if (subject == null || subject.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return repository.findBySubjectContainingIgnoreCase(subject.trim());
    }

    // Search by content
    @Transactional(readOnly = true)
    public List<NursingReports> searchByContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return repository.findByContentContaining(content.trim());
    }

    // Get reports after date
    @Transactional(readOnly = true)
    public List<NursingReports> getReportsAfterDate(LocalDateTime date) {
        return repository.findByCreatedAtAfter(date);
    }

    // Get reports between dates
    @Transactional(readOnly = true)
    public List<NursingReports> getReportsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByCreatedAtBetween(startDate, endDate);
    }

    // Count total reports
    @Transactional(readOnly = true)
    public long countAllReports() {
        return repository.count();
    }

    // Count today's reports
    @Transactional(readOnly = true)
    public long countTodaysReports() {
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return repository.findByCreatedAtBetween(todayStart, todayEnd).size();
    }
}