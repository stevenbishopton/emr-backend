package hospital.emr.bill.repos;

import hospital.emr.bill.entities.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByPatient_Id(Long patientId);
}
