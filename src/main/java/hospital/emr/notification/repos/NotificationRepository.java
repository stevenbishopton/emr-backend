package hospital.emr.notification.repos;

import hospital.emr.notification.model.StoredNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<StoredNotification, Long> {

    List<StoredNotification> findByToDepartmentAndIsDeliveredFalse(String toDepartment);
    List<StoredNotification> findByToDepartmentAndIsReadFalse(String toDepartment);
    List<StoredNotification> findByToDepartmentAndTimestampAfter(String toDepartment, LocalDateTime after);
    List<StoredNotification> findByToDepartmentOrderByTimestampDesc(String toDepartment);

    @Query("SELECT n FROM StoredNotification n WHERE n.toDepartment = :department AND n.isDelivered = false AND n.timestamp >= :since")
    List<StoredNotification> findUndeliveredSince(@Param("department") String department,
                                                  @Param("since") LocalDateTime since);

    long countByToDepartmentAndIsReadFalse(String toDepartment);
}