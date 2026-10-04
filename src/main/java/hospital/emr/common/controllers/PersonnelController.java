package hospital.emr.common.controllers;

import hospital.emr.common.dtos.PersonnelDTO;
import hospital.emr.common.services.PersonnelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/emr/personnel")
public class PersonnelController {

    private final PersonnelService personnelService;

    // ✅ Create Personnel
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PersonnelDTO> createPersonnel(@Valid @RequestBody PersonnelDTO personnelDTO) {
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PersonnelDTO> updatePersonnel(
            @PathVariable Long id,
            @Valid @RequestBody PersonnelDTO personnelDTO) {

        PersonnelDTO updated = personnelService.updatePersonnel(id, personnelDTO);
        return ResponseEntity.ok(updated);
    }

    // ✅ Delete Personnel
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deletePersonnel(@PathVariable Long id) {
        personnelService.deletePersonnel(id);
        return ResponseEntity.noContent().build();
    }
}
