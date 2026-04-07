package hospital.emr.lab.repos;

import hospital.emr.lab.entities.LabTestRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabTestRequestRepository extends JpaRepository<LabTestRequest, Long> {
    List<LabTestRequest> findByPatientId(Long patientId);
    List<LabTestRequest> findByVisitId(Long visitId);
    List<LabTestRequest> findByMedicalHistoryId(Long medicalHistoryId);
    List<LabTestRequest> findByRequestedBy(String requestedBy);

    // FIX: Use native query for JSON array search
    @Query(value = "SELECT * FROM lab_test_request l WHERE JSON_CONTAINS(l.lab_test_ids, :testId) OR l.lab_test_ids LIKE CONCAT('%\"', :testId, '\"%')",
            nativeQuery = true)
    List<LabTestRequest> findByLabTestId(@Param("testId") Long testId);

    @Query("SELECT l FROM LabTestRequest l WHERE l.patientId = :patientId AND l.visitId = :visitId")
    List<LabTestRequest> findByPatientIdAndVisitId(@Param("patientId") Long patientId,
                                                   @Param("visitId") Long visitId);
}