package hospital.emr.lab.repos;

import hospital.emr.lab.entities.LabHistory;
import hospital.emr.lab.enums.TestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LabHistoryRepository extends JpaRepository<LabHistory, Long> {

    List<LabHistory> findByLabTestRequestId(Long labTestRequestId);

    List<LabHistory> findByLabTestResultId(Long labTestResultId);

    List<LabHistory> findByStatus(TestStatus status);

    List<LabHistory> findByPerformedBy(Long performedBy);

    @Query("SELECT lh FROM LabHistory lh WHERE lh.labTestRequestId = :id AND lh.timestamp BETWEEN :start AND :end")
    List<LabHistory> findByLabTestRequestIdAndDateRange(@Param("id") Long id, 
                                                         @Param("start") LocalDateTime start, 
                                                         @Param("end") LocalDateTime end);

    @Query("SELECT lh FROM LabHistory lh WHERE lh.labTestResultId = :id AND lh.timestamp BETWEEN :start AND :end")
    List<LabHistory> findByLabTestResultIdAndDateRange(@Param("id") Long id, 
                                                        @Param("start") LocalDateTime start, 
                                                        @Param("end") LocalDateTime end);

    @Query("SELECT lh FROM LabHistory lh WHERE lh.labTestRequestId = :id AND lh.status = :status")
    List<LabHistory> findByLabTestRequestIdAndStatus(@Param("id") Long id, @Param("status") TestStatus status);

    @Query("SELECT lh FROM LabHistory lh WHERE lh.labTestResultId = :id AND lh.status = :status")
    List<LabHistory> findByLabTestResultIdAndStatus(@Param("id") Long id, @Param("status") TestStatus status);

    boolean existsByLabTestRequestIdAndStatus(Long labTestRequestId, TestStatus status);

    boolean existsByLabTestResultIdAndStatus(Long labTestResultId, TestStatus status);
}
