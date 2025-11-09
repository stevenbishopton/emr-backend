package hospital.emr.reception.controllers;

import hospital.emr.reception.dtos.VisitDepartmentDTO;
import hospital.emr.reception.services.VisitDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
