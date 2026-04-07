package hospital.emr.radiograph.repos;

import hospital.emr.radiograph.entities.RadiographVisitTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RadiographVisitTestRepository extends JpaRepository<RadiographVisitTest, Long> {

    // Find by visit history
    List<RadiographVisitTest> findByVisitHistoryIdOrderByTestNameAsc(Long visitHistoryId);
    
    // Find by status
    List<RadiographVisitTest> findByStatus(String status);
    
    // Find by type
    List<RadiographVisitTest> findByType(String type);
    
    // Find by catalog item
    List<RadiographVisitTest> findByCatalogItemId(Long catalogItemId);
    
    // Find tests performed by date range
    @Query("SELECT rvt FROM RadiographVisitTest rvt WHERE rvt.performedAt BETWEEN :startDate AND :endDate ORDER BY rvt.performedAt DESC")
    List<RadiographVisitTest> findByPerformedAtBetween(@Param("startDate") java.time.LocalDateTime startDate,
                                                      @Param("endDate") java.time.LocalDateTime endDate);
    
    // Find tests with reports
    @Query("SELECT rvt FROM RadiographVisitTest rvt WHERE rvt.reportUrl IS NOT NULL OR rvt.findings IS NOT NULL ORDER BY rvt.reportedAt DESC")
    List<RadiographVisitTest> findTestsWithReports();
    
    // Count tests by status
    long countByStatus(String status);
    
    // Count tests by type
    long countByType(String type);
}
