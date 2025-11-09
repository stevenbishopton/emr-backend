// src/main/java/hospital/emr/reception/controllers/VisitController.java
package hospital.emr.reception.controllers;

import hospital.emr.reception.dtos.NewVisitRequest;
import hospital.emr.reception.dtos.VisitDTO;
import hospital.emr.reception.dtos.VisitDepartmentDTO;
import hospital.emr.reception.enums.VisitStatus;
import hospital.emr.reception.services.VisitDepartmentService;
import hospital.emr.reception.services.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emr/visits")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;
    private final VisitDepartmentService visitDepartmentService;

    @PostMapping
    public ResponseEntity<VisitDTO> createVisit(@RequestBody NewVisitRequest request) {
        VisitDTO createdVisit = visitService.createVisitAndSendToDepartment(request);
        return new ResponseEntity<>(createdVisit, HttpStatus.CREATED);
    }

//    @PostMapping("/department")
//    public ResponseEntity<VisitDepartmentDTO> sendToDepartment
//            (@RequestBody Long visitId, Long departmentId) {
//        VisitDepartmentDTO createdVisit = visitDepartmentService.sendToDepartment(visitId, departmentId);
//        return new ResponseEntity<>(createdVisit, HttpStatus.CREATED);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDTO> getVisitById(@PathVariable Long id) {
        VisitDTO visitDTO = visitService.findVisitById(id);
        return ResponseEntity.ok(visitDTO);
    }

    @GetMapping("/queue")
    public ResponseEntity<List<VisitDTO>> getAllInQueue() {
        List<VisitDTO> visitDTOList = visitService.findInQueue();
        return ResponseEntity.ok(visitDTOList);
    }

    @GetMapping("/admissions")
    public ResponseEntity<List<VisitDTO>> getAllAdmitted() {
        List<VisitDTO> visitDTOList = visitService.findAdmitted();
        return ResponseEntity.ok(visitDTOList);
    }

    @GetMapping("/department/{deptId}/queue")
    public ResponseEntity<List<VisitDTO>> getVisitsInQueueByDepartment(@PathVariable Long deptId) {
        return ResponseEntity.ok(visitService.findVisitsInQueueByDepartment(deptId));
    }


    @PutMapping("/{id}/complete")
    public ResponseEntity<VisitDTO> markVisitAsCompleted(@PathVariable Long id) {
        VisitDTO updatedVisit = visitService.markVisitAsCompleted(id);
        return ResponseEntity.ok(updatedVisit);
    }


    @PutMapping("/{id}/admit")
    public ResponseEntity<VisitDTO> markVisitAsAdmitted(@PathVariable Long id) {
        VisitDTO updatedVisit = visitService.makeVisitAdmission(id);
        return ResponseEntity.ok(updatedVisit);
    }

    @GetMapping
    public ResponseEntity<List<VisitDTO>> getAllVisits(@RequestParam(required = false) Long patientId) {
        List<VisitDTO> visits;
        if (patientId != null) {
            visits = visitService.findVisitsByPatientId(patientId);
        } else {
            visits = visitService.findAllVisits();
        }
        return ResponseEntity.ok(visits);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVisit(@PathVariable Long id) {
        visitService.deleteVisit(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}