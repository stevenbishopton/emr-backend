package hospital.emr.patient.repos;

import hospital.emr.patient.entities.PrescriptionChart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionChartRepository extends JpaRepository<PrescriptionChart, Long> {
    Optional<PrescriptionChart> findByAdmissionId(Long admissionId);

//    // Find prescription charts by prescription ID
//    List<PrescriptionChart> findByPrescriptionId(Long prescriptionId);
//
//    // Find prescription charts by chart type
//    List<PrescriptionChart> findByChartType(String chartType);
//
//    // Find prescription charts by status
//    List<PrescriptionChart> findByStatus(String status);
//
//    // Find prescription charts by prescription and chart type
//    List<PrescriptionChart> findByPrescriptionIdAndChartType(Long prescriptionId, String chartType);
//
//    // Find prescription charts by prescription and status
//    List<PrescriptionChart> findByPrescriptionIdAndStatus(Long prescriptionId, String status);
//
//    // Find active prescription charts
//    @Query("SELECT pc FROM PrescriptionChart pc WHERE pc.status = 'ACTIVE'")
//    List<PrescriptionChart> findActivePrescriptionCharts();
//
//    // Find completed prescription charts
//    @Query("SELECT pc FROM PrescriptionChart pc WHERE pc.status = 'COMPLETED'")
//    List<PrescriptionChart> findCompletedPrescriptionCharts();
//
//    // Count prescription charts by prescription
//    long countByPrescriptionId(Long prescriptionId);
//
//    // Count prescription charts by status
//    long countByStatus(String status);
} 