package hospital.emr.common.repos;

import hospital.emr.common.entities.ScheduledVisitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduledVisitorRepository extends JpaRepository<ScheduledVisitor, Long> {
    List<ScheduledVisitor> findByNamesContainingIgnoreCase(String name);
    List<ScheduledVisitor> findByPhoneNumber(String phoneNumber);
    List<ScheduledVisitor> findByReasonContainingIgnoreCase(String reason);
    List<ScheduledVisitor> findByScheduledVisitorsListId(Long listId);
}