package hospital.emr.common.controllers;

import hospital.emr.common.dtos.NoteDTO;
import hospital.emr.common.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/emr/notes")
@RequiredArgsConstructor
public class NoteController {
    private final NoteService noteService;

    // Get all notes for a visit
    @GetMapping("/visit/{visitId}")
    public ResponseEntity<List<NoteDTO>> getNotesByVisit(@PathVariable Long visitId) {
        List<NoteDTO> notes = noteService.getNotesByVisit(visitId);
        return ResponseEntity.ok(notes);
    }

    // Get admission record for a visit
    @GetMapping("/visit/{visitId}/admission-record")
    public ResponseEntity<NoteDTO> getAdmissionRecordByVisit(@PathVariable Long visitId) {
        NoteDTO admissionRecord = noteService.getAdmissionRecordByVisit(visitId);
        if (admissionRecord != null) {
            return ResponseEntity.ok(admissionRecord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get note by ID
    @GetMapping("/{noteId}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable Long noteId) {
        NoteDTO note = noteService.getNoteById(noteId);
        return ResponseEntity.ok(note);
    }

    // Create a new note
    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@Valid @RequestBody NoteDTO noteDTO) {
        System.out.println("📝 Creating note with data: " + noteDTO);
        NoteDTO createdNote = noteService.createNote(noteDTO);
        return new ResponseEntity<>(createdNote, HttpStatus.CREATED);
    }

    // UPDATE a note
    @PutMapping("/{noteId}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Long noteId, @Valid @RequestBody NoteDTO noteDTO) {
        System.out.println("📝 Updating note with ID: " + noteId + ", data: " + noteDTO);
        NoteDTO updatedNote = noteService.updateNote(noteId, noteDTO);
        return ResponseEntity.ok(updatedNote);
    }

    // DELETE a note
    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        System.out.println("📝 Deleting note with ID: " + noteId);
        noteService.deleteNote(noteId);
        return ResponseEntity.noContent().build();
    }

}