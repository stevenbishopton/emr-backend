package hospital.emr.common.services;

import hospital.emr.common.entities.ScheduledVisitorsList;
import hospital.emr.common.repos.ScheduledVisitorsListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduledVisitorsListService {

    private final ScheduledVisitorsListRepository scheduledVisitorsListRepository;

    public List<ScheduledVisitorsList> findAll() {
        return scheduledVisitorsListRepository.findAll();
    }

    public Optional<ScheduledVisitorsList> findById(Long id) {
        return scheduledVisitorsListRepository.findById(id);
    }

    public ScheduledVisitorsList save(ScheduledVisitorsList scheduledVisitorsList) {
        return scheduledVisitorsListRepository.save(scheduledVisitorsList);
    }

    public void deleteById(Long id) {
        scheduledVisitorsListRepository.deleteById(id);
    }

    public List<ScheduledVisitorsList> findByDepartment(String department) {
        return scheduledVisitorsListRepository.findByDepartment(department);
    }

    public Optional<ScheduledVisitorsList> findByDepartmentContaining(String department) {
        return scheduledVisitorsListRepository.findByDepartmentContainingIgnoreCase(department);
    }
}