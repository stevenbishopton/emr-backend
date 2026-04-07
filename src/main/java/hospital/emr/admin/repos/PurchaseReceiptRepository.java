package hospital.emr.admin.repos;

import hospital.emr.admin.entities.PurchaseReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseReceiptRepository extends JpaRepository<PurchaseReceipt, Long> {

    List<PurchaseReceipt> findByClientNameContainingIgnoreCase(String clientName);

    List<PurchaseReceipt> findByOrderedByContainingIgnoreCase(String orderedBy);

    @Query("SELECT pr FROM PurchaseReceipt pr WHERE pr.totalAmount > :minAmount")
    List<PurchaseReceipt> findByTotalAmountGreaterThan(@Param("minAmount") String minAmount);

    @Query("SELECT pr FROM PurchaseReceipt pr ORDER BY pr.id DESC")
    List<PurchaseReceipt> findAllOrderByIdDesc();
}