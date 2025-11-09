package hospital.emr.common.controllers;

import hospital.emr.common.entities.ScheduledVisitor;
import hospital.emr.common.services.ScheduledVisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/emr/scheduled-visitors")
@RequiredArgsConstructor
public class ScheduledVisitorController {

    private final ScheduledVisitorService scheduledVisitorService;

    @GetMapping
    public ResponseEntity<List<ScheduledVisitor>> getAllScheduledVisitors() {
        List<ScheduledVisitor> visitors = scheduledVisitorService.findAll();
        return ResponseEntity.ok(visitors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduledVisitor> getScheduledVisitorById(@PathVariable Long id) {
        Optional<ScheduledVisitor> visitor = scheduledVisitorService.findById(id);
        return visitor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ScheduledVisitor> createScheduledVisitor(@RequestBody ScheduledVisitor scheduledVisitor) {
        ScheduledVisitor savedVisitor = scheduledVisitorService.save(scheduledVisitor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVisitor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduledVisitor> updateScheduledVisitor(@PathVariable Long id, @RequestBody ScheduledVisitor scheduledVisitor) {
        if (scheduledVisitorService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        scheduledVisitor.setId(id);
        ScheduledVisitor updatedVisitor = scheduledVisitorService.save(scheduledVisitor);
        return ResponseEntity.ok(updatedVisitor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduledVisitor(@PathVariable Long id) {
        if (scheduledVisitorService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        scheduledVisitorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/name/{name}")
    public ResponseEntity<List<ScheduledVisitor>> getScheduledVisitorsByName(@PathVariable String name) {
        List<ScheduledVisitor> visitors = scheduledVisitorService.findByName(name);
        return ResponseEntity.ok(visitors);
    }

    @GetMapping("/search/phone/{phoneNumber}")
    public ResponseEntity<List<ScheduledVisitor>> getScheduledVisitorsByPhoneNumber(@PathVariable String phoneNumber) {
        List<ScheduledVisitor> visitors = scheduledVisitorService.findByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(visitors);
    }

    @GetMapping("/search/reason/{reason}")
    public ResponseEntity<List<ScheduledVisitor>> getScheduledVisitorsByReason(@PathVariable String reason) {
        List<ScheduledVisitor> visitors = scheduledVisitorService.findByReason(reason);
        return ResponseEntity.ok(visitors);
    }

    @GetMapping("/list/{listId}")
    public ResponseEntity<List<ScheduledVisitor>> getScheduledVisitorsByListId(@PathVariable Long listId) {
        List<ScheduledVisitor> visitors = scheduledVisitorService.findByScheduledVisitorsListId(listId);
        return ResponseEntity.ok(visitors);
    }
}