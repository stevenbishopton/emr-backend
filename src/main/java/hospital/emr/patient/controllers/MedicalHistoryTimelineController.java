package hospital.emr.patient.controllers;

import hospital.emr.patient.dtos.VisitTimelineDTO;
import hospital.emr.patient.services.MedicalHistoryTimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emr/patients")
@RequiredArgsConstructor
public class MedicalHistoryTimelineController {

    private final MedicalHistoryTimelineService timelineService;

    @GetMapping("/{patientId}/timeline")
    public ResponseEntity<List<VisitTimelineDTO>> getPatientTimeline(@PathVariable Long patientId) {
        return ResponseEntity.ok(timelineService.getTimelineForPatient(patientId));
    }
}
