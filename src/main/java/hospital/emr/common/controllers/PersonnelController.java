package hospital.emr.common.controllers;

import hospital.emr.common.dtos.PersonnelDTO;
import hospital.emr.common.services.PersonnelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emr/personnel")
public class PersonnelController {

    private final PersonnelService personnelService;

    // ✅ Create Personnel
    @PostMapping
    public ResponseEntity<PersonnelDTO> createPersonnel(@RequestBody PersonnelDTO personnelDTO) {
        PersonnelDTO created = personnelService.createPersonnel(personnelDTO);
        return ResponseEntity.ok(created);
    }

    // ✅ Get All Personnel
    @GetMapping
    public ResponseEntity<List<PersonnelDTO>> getAllPersonnel() {
        return ResponseEntity.ok(personnelService.getAllPersonnel());
    }

    // ✅ Get Personnel by ID
    @GetMapping("/{id}")
    public ResponseEntity<PersonnelDTO> getPersonnelById(@PathVariable Long id) {
        PersonnelDTO personnel = personnelService.getPersonnelById(id);
        return ResponseEntity.ok(personnel);
    }

    // ✅ Update Personnel
    @PutMapping("/{id}")
    public ResponseEntity<PersonnelDTO> updatePersonnel(
            @PathVariable Long id,
            @RequestBody PersonnelDTO personnelDTO) {

        PersonnelDTO updated = personnelService.updatePersonnel(id, personnelDTO);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete Personnel
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonnel(@PathVariable Long id) {
        personnelService.deletePersonnel(id);
        return ResponseEntity.noContent().build();
    }
}
