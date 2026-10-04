package hospital.emr.lab.repos;

import hospital.emr.lab.entities.LabOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabOrderItemRepository extends JpaRepository<LabOrderItem, Long> {

    List<LabOrderItem> findByLabOrderId(Long orderId);
}
