package hospital.emr.common.services;

import hospital.emr.common.entities.ScheduledVisitor;
import hospital.emr.common.repos.ScheduledVisitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduledVisitorService {

    private final ScheduledVisitorRepository scheduledVisitorRepository;

    public List<ScheduledVisitor> findAll() {
        return scheduledVisitorRepository.findAll();
    }

    public Optional<ScheduledVisitor> findById(Long id) {
        return scheduledVisitorRepository.findById(id);
    }

    public ScheduledVisitor save(ScheduledVisitor scheduledVisitor) {
        return scheduledVisitorRepository.save(scheduledVisitor);
    }

    public void deleteById(Long id) {
        scheduledVisitorRepository.deleteById(id);
    }

    public List<ScheduledVisitor> findByName(String name) {
        return scheduledVisitorRepository.findByNamesContainingIgnoreCase(name);
    }

    public List<ScheduledVisitor> findByPhoneNumber(String phoneNumber) {
        return scheduledVisitorRepository.findByPhoneNumber(phoneNumber);
    }

    public List<ScheduledVisitor> findByReason(String reason) {
        return scheduledVisitorRepository.findByReasonContainingIgnoreCase(reason);
    }

    public List<ScheduledVisitor> findByScheduledVisitorsListId(Long listId) {
        return scheduledVisitorRepository.findByScheduledVisitorsListId(listId);
    }
}