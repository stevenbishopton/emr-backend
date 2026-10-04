package hospital.emr.lab.repos;

import hospital.emr.lab.entities.LabAnalyteTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabAnalyteTemplateRepository extends JpaRepository<LabAnalyteTemplate, Long> {

    List<LabAnalyteTemplate> findByTestIdAndActiveTrueOrderByDisplayOrderAsc(Long testId);

    List<LabAnalyteTemplate> findByTestIdInOrderByDisplayOrderAsc(List<Long> testIds);

    boolean existsByTestId(Long testId);
}
