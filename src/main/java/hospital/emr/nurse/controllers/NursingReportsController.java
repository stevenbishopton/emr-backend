package hospital.emr.nurse.controllers;

import hospital.emr.nurse.entities.NursingReports;
import hospital.emr.nurse.services.NursingReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/emr/nursing-reports")
@RequiredArgsConstructor
public class NursingReportsController {

    private final NursingReportsService nursingReportsService;

    // Create a new report
    @PostMapping
    public ResponseEntity<NursingReports> createReport(@RequestBody NursingReports report) {
        try {
            NursingReports createdReport = nursingReportsService.createReport(report);
            return new ResponseEntity<>(createdReport, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Get report by ID
    @GetMapping("/{id}")
    public ResponseEntity<NursingReports> getReportById(@PathVariable Long id) {
        try {
            NursingReports report = nursingReportsService.getReportById(id);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get all reports
    @GetMapping
    public ResponseEntity<List<NursingReports>> getAllReports() {
        try {
            List<NursingReports> reports = nursingReportsService.getAllReports();
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get recent reports
    @GetMapping("/recent")
    public ResponseEntity<List<NursingReports>> getRecentReports() {
        try {
            List<NursingReports> reports = nursingReportsService.getRecentReports();
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update report
    @PutMapping("/{id}")
    public ResponseEntity<NursingReports> updateReport(@PathVariable Long id, @RequestBody NursingReports report) {
        try {
            NursingReports updatedReport = nursingReportsService.updateReport(id, report);
            return ResponseEntity.ok(updatedReport);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Delete report
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        try {
            nursingReportsService.deleteReport(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Advanced search
    @GetMapping("/search")
    public ResponseEntity<List<NursingReports>> searchReports(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<NursingReports> reports = nursingReportsService.searchReports(author, subject, content, startDate, endDate);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get reports by author
    @GetMapping("/author/{author}")
    public ResponseEntity<List<NursingReports>> getReportsByAuthor(@PathVariable String author) {
        try {
            List<NursingReports> reports = nursingReportsService.getReportsByAuthor(author);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search by subject
    @GetMapping("/search/subject")
    public ResponseEntity<List<NursingReports>> searchBySubject(@RequestParam String subject) {
        try {
            List<NursingReports> reports = nursingReportsService.searchBySubject(subject);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Search by content
    @GetMapping("/search/content")
    public ResponseEntity<List<NursingReports>> searchByContent(@RequestParam String content) {
        try {
            List<NursingReports> reports = nursingReportsService.searchByContent(content);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get reports after date
    @GetMapping("/date/after")
    public ResponseEntity<List<NursingReports>> getReportsAfterDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            List<NursingReports> reports = nursingReportsService.getReportsAfterDate(date);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get reports between dates
    @GetMapping("/date/between")
    public ResponseEntity<List<NursingReports>> getReportsBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<NursingReports> reports = nursingReportsService.getReportsBetweenDates(startDate, endDate);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Statistics
    @GetMapping("/stats")
    public ResponseEntity<Object> getStatistics() {
        try {
            long totalReports = nursingReportsService.countAllReports();
            long todaysReports = nursingReportsService.countTodaysReports();

            return ResponseEntity.ok(new Object() {
                public final long total = totalReports;
                public final long today = todaysReports;
            });
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Nursing Reports API is working!");
    }
}