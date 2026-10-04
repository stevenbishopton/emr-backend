package hospital.emr.lab.repos;

import hospital.emr.lab.entities.LabAnalyteResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabAnalyteResultRepository extends JpaRepository<LabAnalyteResult, Long> {

    List<LabAnalyteResult> findByOrderItemId(Long orderItemId);
}
