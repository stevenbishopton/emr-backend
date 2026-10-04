package hospital.emr.lab.repos;

import hospital.emr.lab.entities.LabSpecimen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabSpecimenRepository extends JpaRepository<LabSpecimen, Long> {

    Optional<LabSpecimen> findByAccessionNumber(String accessionNumber);

    List<LabSpecimen> findByLabOrderId(Long orderId);

    boolean existsByAccessionNumber(String accessionNumber);
}
