package hospital.emr.radiograph.services;

import hospital.emr.radiograph.dtos.RadiographVisitHistoryDTO;
import hospital.emr.radiograph.enums.RadiographStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RadiographVisitHistoryService {

    // CRUD operations
    RadiographVisitHistoryDTO createVisitHistory(RadiographVisitHistoryDTO dto);
    RadiographVisitHistoryDTO updateVisitHistory(Long id, RadiographVisitHistoryDTO dto);
    Optional<RadiographVisitHistoryDTO> getVisitHistoryById(Long id);
    void deleteVisitHistory(Long id);
    List<RadiographVisitHistoryDTO> getAllVisitHistories();
    Page<RadiographVisitHistoryDTO> getAllVisitHistories(Pageable pageable);

    // Patient-specific operations
    List<RadiographVisitHistoryDTO> getPatientRadiographHistory(Long patientId);
    Page<RadiographVisitHistoryDTO> getPatientRadiographHistory(Long patientId, Pageable pageable);
    List<RadiographVisitHistoryDTO> getPatientRadiographHistoryByDateRange(Long patientId, LocalDateTime startDate, LocalDateTime endDate);

    // Visit-specific operations
    Optional<RadiographVisitHistoryDTO> getVisitHistoryByVisitId(Long visitId);

    // Status-based operations
    List<RadiographVisitHistoryDTO> getVisitHistoriesByStatus(RadiographStatus status);
    Page<RadiographVisitHistoryDTO> getVisitHistoriesByStatus(RadiographStatus status, Pageable pageable);
    List<RadiographVisitHistoryDTO> getVisitsRequiringAttention();
    List<RadiographVisitHistoryDTO> getCompletedVisitsWithReports();

    // Personnel-specific operations
    List<RadiographVisitHistoryDTO> getVisitHistoriesByRequestedBy(Long requestedById);
    List<RadiographVisitHistoryDTO> getVisitHistoriesByPerformedBy(Long performedById);
    List<RadiographVisitHistoryDTO> getVisitHistoriesByRadiologist(Long radiologistId);

    // Date-based operations
    List<RadiographVisitHistoryDTO> getVisitHistoriesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Page<RadiographVisitHistoryDTO> getVisitHistoriesByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    List<RadiographVisitHistoryDTO> getRecentVisits(int days);

    // Search operations
    List<RadiographVisitHistoryDTO> searchByPatient(String searchTerm);
    Page<RadiographVisitHistoryDTO> searchByPatient(String searchTerm, Pageable pageable);

    // Visit type operations
    List<RadiographVisitHistoryDTO> getVisitHistoriesByVisitType(String visitType);

    // Statistics operations
    long countByStatus(RadiographStatus status);
    List<Object[]> getVisitStatistics();

    // Status update operations
    RadiographVisitHistoryDTO updateStatus(Long id, RadiographStatus newStatus, Long performedById, String notes);

    // Report operations
    RadiographVisitHistoryDTO addRadiologistReport(Long id, String report, Long radiologistId);
    RadiographVisitHistoryDTO addTechnicianNotes(Long id, String notes, Long technicianId);

    // Utility operations
    boolean existsById(Long id);
    boolean existsByVisitId(Long visitId);
}
