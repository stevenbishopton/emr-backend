package hospital.emr.pharmacy.repos;

import hospital.emr.pharmacy.dto.PxNurseDeduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PxNurseDeductionRepository extends JpaRepository<PxNurseDeduction, Long> {
    List<PxNurseDeduction> findByItemRequestId(Long itemRequestId);
    List<PxNurseDeduction> findByDispenserContainingIgnoreCase(String dispenser);
}