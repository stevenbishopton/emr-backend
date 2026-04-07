package hospital.emr.reception.controllers;

import hospital.emr.reception.dtos.QueueFilterRequest;
import hospital.emr.reception.dtos.VisitDepartmentDTO;
import hospital.emr.reception.enums.VisitStatus;
import hospital.emr.reception.services.VisitDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/emr/visit-dept")
@RequiredArgsConstructor
public class VisitDepartmentController {

    private final VisitDepartmentService visitDepartmentService;

    @PostMapping("/{visitId}/departments/{departmentId}")
    public ResponseEntity<VisitDepartmentDTO> sendToDepartment(
            @PathVariable Long visitId,
            @PathVariable Long departmentId) {

        VisitDepartmentDTO dto = visitDepartmentService.sendToDepartment(visitId, departmentId);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    // NEW: Get departments by visit
    @GetMapping("/{visitId}/departments")
    public ResponseEntity<List<VisitDepartmentDTO>> getDepartmentsByVisit(@PathVariable Long visitId) {
        List<VisitDepartmentDTO> departments = visitDepartmentService.findDepartmentsByVisitId(visitId);
        return ResponseEntity.ok(departments);
    }

    // NEW: Get visits by department (without filters - all visits)
    @GetMapping("/department/{deptId}/visits")
    public ResponseEntity<List<VisitDepartmentDTO>> getVisitsByDepartment(@PathVariable Long deptId) {
        List<VisitDepartmentDTO> visits = visitDepartmentService.findVisitsByDepartmentId(deptId);
        return ResponseEntity.ok(visits);
    }

    // Filtered department queue
    @GetMapping("/department/{deptId}/queue")
    public ResponseEntity<List<VisitDepartmentDTO>> getDepartmentQueue(
            @PathVariable Long deptId,
            @RequestParam(required = false) VisitStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Boolean today) {

        QueueFilterRequest filters = new QueueFilterRequest();
        filters.setStatus(status);
        filters.setDate(date);
        filters.setTodayOnly(today);

        List<VisitDepartmentDTO> queue = visitDepartmentService.findDepartmentQueueWithFilters(deptId, filters);
        return ResponseEntity.ok(queue);
    }
}