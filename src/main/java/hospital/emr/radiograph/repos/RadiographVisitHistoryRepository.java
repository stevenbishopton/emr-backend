package hospital.emr.radiograph.repos;

import hospital.emr.radiograph.entities.RadiographVisitHistory;
import hospital.emr.radiograph.enums.RadiographStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RadiographVisitHistoryRepository extends JpaRepository<RadiographVisitHistory, Long> {

    // Find by patient
    List<RadiographVisitHistory> findByPatientIdOrderByVisitDateDesc(Long patientId);
    
    Page<RadiographVisitHistory> findByPatientIdOrderByVisitDateDesc(Long patientId, Pageable pageable);
    
    // Find by visit
    Optional<RadiographVisitHistory> findByVisitId(Long visitId);
    
    // Find by status
    List<RadiographVisitHistory> findByStatusOrderByVisitDateDesc(RadiographStatus status);
    
    Page<RadiographVisitHistory> findByStatusOrderByVisitDateDesc(RadiographStatus status, Pageable pageable);
    
    // Find by requested by personnel
    List<RadiographVisitHistory> findByRequestedByIdOrderByVisitDateDesc(Long requestedById);
    
    // Find by performed by personnel
    List<RadiographVisitHistory> findByPerformedByIdOrderByVisitDateDesc(Long performedById);
    
    // Find by radiologist
    List<RadiographVisitHistory> findByRadiologistIdOrderByVisitDateDesc(Long radiologistId);
    
    // Find by date range
    @Query("SELECT rvh FROM RadiographVisitHistory rvh WHERE rvh.visitDate BETWEEN :startDate AND :endDate ORDER BY rvh.visitDate DESC")
    List<RadiographVisitHistory> findByVisitDateBetween(@Param("startDate") LocalDateTime startDate, 
                                                       @Param("endDate") LocalDateTime endDate);
    
    Page<RadiographVisitHistory> findByVisitDateBetweenOrderByVisitDateDesc(LocalDateTime startDate, 
                                                                            LocalDateTime endDate, 
                                                                            Pageable pageable);
    
    // Find by patient and date range
    @Query("SELECT rvh FROM RadiographVisitHistory rvh WHERE rvh.patient.id = :patientId AND rvh.visitDate BETWEEN :startDate AND :endDate ORDER BY rvh.visitDate DESC")
    List<RadiographVisitHistory> findByPatientIdAndVisitDateBetween(@Param("patientId") Long patientId,
                                                                   @Param("startDate") LocalDateTime startDate,
                                                                   @Param("endDate") LocalDateTime endDate);
    
    // Find by visit type
    List<RadiographVisitHistory> findByVisitTypeOrderByVisitDateDesc(String visitType);
    
    // Count by status
    long countByStatus(RadiographStatus status);
    
    // Find recent visits (last N days)
    @Query("SELECT rvh FROM RadiographVisitHistory rvh WHERE rvh.visitDate >= :since ORDER BY rvh.visitDate DESC")
    List<RadiographVisitHistory> findRecentVisits(@Param("since") LocalDateTime since);
    
    // Search by patient name or ID
    @Query("SELECT rvh FROM RadiographVisitHistory rvh WHERE LOWER(rvh.patient.names) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR CAST(rvh.patient.id AS string) LIKE CONCAT('%', :searchTerm, '%') ORDER BY rvh.visitDate DESC")
    List<RadiographVisitHistory> searchByPatient(@Param("searchTerm") String searchTerm);
    
    // Search by patient name or ID with pagination
    @Query("SELECT rvh FROM RadiographVisitHistory rvh WHERE LOWER(rvh.patient.names) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR CAST(rvh.patient.id AS string) LIKE CONCAT('%', :searchTerm, '%') ORDER BY rvh.visitDate DESC")
    Page<RadiographVisitHistory> searchByPatient(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    // Find completed visits with reports
    @Query("SELECT rvh FROM RadiographVisitHistory rvh WHERE rvh.status = 'COMPLETED' AND rvh.radiologistReport IS NOT NULL ORDER BY rvh.completedDate DESC")
    List<RadiographVisitHistory> findCompletedVisitsWithReports();
    
    // Find visits requiring attention (pending, in progress)
    @Query("SELECT rvh FROM RadiographVisitHistory rvh WHERE rvh.status IN ('REQUESTED', 'SCHEDULED', 'IN_PROGRESS') ORDER BY rvh.visitDate ASC")
    List<RadiographVisitHistory> findVisitsRequiringAttention();
}
