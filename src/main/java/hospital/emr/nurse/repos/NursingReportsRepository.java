package hospital.emr.nurse.repos;

import hospital.emr.nurse.entities.NursingReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NursingReportsRepository extends JpaRepository<NursingReports, Long> {

    // Find by author
    List<NursingReports> findByAuthor(String author);

    // Find by author containing (case-insensitive)
    List<NursingReports> findByAuthorContainingIgnoreCase(String author);

    // Find by subject containing (case-insensitive)
    List<NursingReports> findBySubjectContainingIgnoreCase(String subject);

    // Find by content containing (using TEXT column)
    @Query("SELECT n FROM NursingReports n WHERE n.content LIKE %:keyword%")
    List<NursingReports> findByContentContaining(@Param("keyword") String keyword);

    // Find by created date after
    List<NursingReports> findByCreatedAtAfter(LocalDateTime date);

    // Find by created date between
    List<NursingReports> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find by author and date range
    List<NursingReports> findByAuthorAndCreatedAtBetween(String author, LocalDateTime startDate, LocalDateTime endDate);

    // Find by subject and date range
    List<NursingReports> findBySubjectContainingIgnoreCaseAndCreatedAtBetween(String subject, LocalDateTime startDate, LocalDateTime endDate);

    // Find recent reports (ordered by createdAt descending)
    List<NursingReports> findTop10ByOrderByCreatedAtDesc();
}