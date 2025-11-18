package hospital.emr.pharmacy.repos;

import hospital.emr.pharmacy.dto.PxNurseItemRequestDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PxNurseItemRequestRepository extends JpaRepository<PxNurseItemRequestDTO, Long> {

    List<PxNurseItemRequestDTO> findByPatientId(Long patientId);

    List<PxNurseItemRequestDTO> findByVisitId(Long visitId);

    List<PxNurseItemRequestDTO> findByWardId(Long wardId);

    List<PxNurseItemRequestDTO> findByPatientCode(String patientCode);

    List<PxNurseItemRequestDTO> findByPatientNamesContainingIgnoreCase(String patientNames);
}