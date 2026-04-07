// hospital/emr/radiograph/repository/RadiographRepository.java
package hospital.emr.radiograph.repos;

import hospital.emr.radiograph.entities.Radiograph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RadiographRepository extends JpaRepository<Radiograph, Long> {

    List<Radiograph> findByPatientId(Long patientId);

    List<Radiograph> findByVisitId(Long visitId);

    List<Radiograph> findByMedicalHistoryId(Long medicalHistoryId);

    List<Radiograph> findByRadiographType(hospital.emr.radiograph.enums.RadiographType radiographType);

    @Query("SELECT r FROM Radiograph r WHERE r.patientId = :patientId AND r.radiographType = :radiographType")
    List<Radiograph> findByPatientIdAndRadiographType(
            @Param("patientId") Long patientId,
            @Param("radiographType") hospital.emr.radiograph.enums.RadiographType radiographType
    );

    Optional<Radiograph> findByIdAndPatientId(Long id, Long patientId);
}