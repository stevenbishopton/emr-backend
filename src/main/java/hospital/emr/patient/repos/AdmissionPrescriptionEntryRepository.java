package hospital.emr.patient.repos;

import hospital.emr.patient.entities.AdmissionPrescriptionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionPrescriptionEntryRepository extends JpaRepository<AdmissionPrescriptionEntry, Long> {

} 