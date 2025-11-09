package hospital.emr.common.services;

import hospital.emr.common.dtos.NoteDTO;
import hospital.emr.common.entities.Note;
import hospital.emr.common.enums.NoteType;
import hospital.emr.common.mappers.NoteMapper;
import hospital.emr.common.repos.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    @Transactional(readOnly = true)
    public List<NoteDTO> getNotesByVisit(Long visitId) {
        return noteRepository.findByVisitIdOrderByCreatedAtDesc(visitId)
                .stream()
                .map(noteMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public NoteDTO getNoteById(Long noteId) {
        return noteRepository.findById(noteId)
                .map(noteMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Note not found with ID: " + noteId));
    }

    // ADD THIS METHOD: Get the Note entity (not DTO) for setting relationships
    @Transactional(readOnly = true)
    public Note getNoteEntityById(Long noteId) {
        return noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found with ID: " + noteId));
    }

    @Transactional(readOnly = true)
    public NoteDTO getAdmissionRecordByVisit(Long visitId) {
        List<Note> visitNotes = noteRepository.findByVisitIdOrderByCreatedAtDesc(visitId);

        Optional<Note> admissionRecord = visitNotes.stream()
                .filter(note -> note.getNoteType() == NoteType.ADMISSION)
                .findFirst();

        return admissionRecord.map(noteMapper::toDto).orElse(null);
    }

    @Transactional
    public NoteDTO createNote(NoteDTO noteDTO) {
        Note note = noteMapper.toEntity(noteDTO);
        Note savedNote = noteRepository.save(note);
        return noteMapper.toDto(savedNote);
    }

    // ADD UPDATE METHOD
    @Transactional
    public NoteDTO updateNote(Long noteId, NoteDTO noteDTO) {
        Note existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found with ID: " + noteId));

        // Update fields if provided in the DTO
        if (noteDTO.getContent() != null) {
            existingNote.setContent(noteDTO.getContent());
        }
        if (noteDTO.getAuthor() != null) {
            existingNote.setAuthor(noteDTO.getAuthor());
        }
        if (noteDTO.getNoteType() != null) {
            existingNote.setNoteType(noteDTO.getNoteType());
        }
        // Note: createdAt is not updated - it remains the original creation time
        // Note: medicalHistory and visit relationships are not updated here to maintain data integrity

        Note updatedNote = noteRepository.save(existingNote);
        return noteMapper.toDto(updatedNote);
    }

    // ADD DELETE METHOD
    @Transactional
    public void deleteNote(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found with ID: " + noteId));

        // Check if this note is being used as an admission record
        // You might want to add this check if you have a way to query admissions by note
        // For now, we'll delete it and let the database constraints handle it

        noteRepository.delete(note);
    }


}