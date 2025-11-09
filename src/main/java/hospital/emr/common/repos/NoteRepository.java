package hospital.emr.common.repos;

import hospital.emr.common.entities.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByVisitIdOrderByCreatedAtDesc(Long visitId);
}