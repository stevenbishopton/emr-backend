package hospital.emr.lab.repos;

import hospital.emr.lab.entities.LabTestResult;
import hospital.emr.lab.enums.TestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for LabTestResult entities
 */
@Repository
public interface LabTestResultRepository extends JpaRepository<LabTestResult, Long> {

    /**
     * Find all results for a patient
     */
    List<LabTestResult> findByPatientId(Long patientId);

    /**
     * Find all results for a visit
     */
    List<LabTestResult> findByVisitId(Long visitId);

    /**
     * Find results by status
     */
    List<LabTestResult> findByStatus(TestStatus status);

    /**
     * Find pending results (not completed or verified)
     */
    @Query("SELECT r FROM LabTestResult r WHERE r.status IN :statuses")
    List<LabTestResult> findPendingResults(@Param("statuses") List<TestStatus> statuses);

    /**
     * Find results by patient and status
     */
    List<LabTestResult> findByPatientIdAndStatus(Long patientId, TestStatus status);


    /**
     * Find results created between dates
     */
    List<LabTestResult> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find results for a patient within a date range
     */
    @Query("SELECT r FROM LabTestResult r WHERE r.patientId = :patientId AND r.createdAt BETWEEN :startDate AND :endDate")
    List<LabTestResult> findByPatientIdAndCreatedAtBetween(
        @Param("patientId") Long patientId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    /**
     * Find results that contain a specific test ID
     */
    @Query("SELECT r FROM LabTestResult r WHERE :testId MEMBER OF r.testIds")
    List<LabTestResult> findByTestId(@Param("testId") Long testId);
}
