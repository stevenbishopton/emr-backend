package hospital.emr.radiograph.repos;

import hospital.emr.radiograph.entities.RadiographHistory;
import hospital.emr.radiograph.enums.RadiographStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RadiographHistoryRepository extends JpaRepository<RadiographHistory, Long> {
    
    List<RadiographHistory> findByRadiographIdOrderByTimestampDesc(Long radiographId);
    
    List<RadiographHistory> findByRadiographIdAndStatus(Long radiographId, RadiographStatus status);
    
    @Query("SELECT h FROM RadiographHistory h WHERE h.radiographId = :radiographId AND h.timestamp BETWEEN :start AND :end ORDER BY h.timestamp DESC")
    List<RadiographHistory> findByRadiographIdAndDateRange(@Param("radiographId") Long radiographId,
                                                         @Param("start") LocalDateTime start,
                                                         @Param("end") LocalDateTime end);
    
    @Query("SELECT h FROM RadiographHistory h WHERE h.performedBy = :userId ORDER BY h.timestamp DESC")
    List<RadiographHistory> findByPerformedBy(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(h) FROM RadiographHistory h WHERE h.radiographId = :radiographId")
    long countByRadiographId(@Param("radiographId") Long radiographId);
}
