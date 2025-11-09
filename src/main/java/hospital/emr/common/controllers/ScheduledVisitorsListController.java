package hospital.emr.common.controllers;

import hospital.emr.common.entities.ScheduledVisitorsList;
import hospital.emr.common.services.ScheduledVisitorsListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emr/scheduled-visitors-lists")
@RequiredArgsConstructor
public class ScheduledVisitorsListController {

    private final ScheduledVisitorsListService scheduledVisitorsListService;

    @GetMapping
    public ResponseEntity<List<ScheduledVisitorsList>> getAllScheduledVisitorsLists() {
        List<ScheduledVisitorsList> lists = scheduledVisitorsListService.findAll();
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledVisitorsList> getScheduledVisitorsListById(@PathVariable Long id) {
        Optional<ScheduledVisitorsList> list = scheduledVisitorsListService.findById(id);
        return list.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ScheduledVisitorsList> createScheduledVisitorsList(@RequestBody ScheduledVisitorsList scheduledVisitorsList) {
        ScheduledVisitorsList savedList = scheduledVisitorsListService.save(scheduledVisitorsList);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduledVisitorsList> updateScheduledVisitorsList(@PathVariable Long id, @RequestBody ScheduledVisitorsList scheduledVisitorsList) {
        if (!scheduledVisitorsListService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        scheduledVisitorsList.setId(id);
        ScheduledVisitorsList updatedList = scheduledVisitorsListService.save(scheduledVisitorsList);
        return ResponseEntity.ok(updatedList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduledVisitorsList(@PathVariable Long id) {
        if (!scheduledVisitorsListService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        scheduledVisitorsListService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<ScheduledVisitorsList>> getScheduledVisitorsListsByDepartment(@PathVariable String department) {
        List<ScheduledVisitorsList> lists = scheduledVisitorsListService.findByDepartment(department);
        return ResponseEntity.ok(lists);
    }
}