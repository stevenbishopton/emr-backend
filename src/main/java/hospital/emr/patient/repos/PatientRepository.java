package hospital.emr.patient.repos;

import hospital.emr.patient.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Finds patients whose names contain the given search term (case-insensitive).
     *
     * @param searchTerm The search term (e.g., "david si").
     * @return A list of matching patients.
     */
    @Query("SELECT p FROM Patient p WHERE lower(p.names) LIKE lower(concat('%', :searchTerm, '%'))")
    Optional<Patient> findByNamesContainingIgnoreCase(@Param("searchTerm") String searchTerm);
    Optional<Patient> findByPhoneNumber(String phoneNumber);
    Optional<Patient> findByCode(String code);


//    // Find patient by email
//    Optional<Patient> findByEmail(String email);
//
//    // Find patients by name (partial match)
//    List<Patient> findByNamesContainingIgnoreCase(String names);
//    Page<Patient> findByNamesContainingIgnoreCase(String names, Pageable pageable);
//
//    // Find patients by sex
//    List<Patient> findBySex(hospital.emr.common.enums.Sex sex);
//    Page<Patient> findBySex(hospital.emr.common.enums.Sex sex, Pageable pageable);
//
//    // Find patients by date of birth
//    List<Patient> findByDateOfBirth(LocalDate dateOfBirth);
//    Page<Patient> findByDateOfBirth(LocalDate dateOfBirth, Pageable pageable);
//
//    // Find patients born between dates
//    List<Patient> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);
//    Page<Patient> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
//
//    // Find patients by occupation
//    List<Patient> findByOccupationContainingIgnoreCase(String occupation);
//    Page<Patient> findByOccupationContainingIgnoreCase(String occupation, Pageable pageable);
//
//    // Custom query to find patients with bills
//    @Query("SELECT p FROM Patient p WHERE p.bills IS NOT EMPTY")
//    List<Patient> findPatientsWithBills();
//    @Query("SELECT p FROM Patient p WHERE p.bills IS NOT EMPTY")
//    Page<Patient> findPatientsWithBills(Pageable pageable);
//
//    // Custom query to find patients without medical history
//    @Query("SELECT p FROM Patient p WHERE p.medicalHistory IS NULL")
//    List<Patient> findPatientsWithoutMedicalHistory();
//    @Query("SELECT p FROM Patient p WHERE p.medicalHistory IS NULL")
//    Page<Patient> findPatientsWithoutMedicalHistory(Pageable pageable);
//
//    // Custom query to find patients by age range
//    @Query("SELECT p FROM Patient p WHERE YEAR(CURRENT_DATE) - YEAR(p.dateOfBirth) BETWEEN :minAge AND :maxAge")
//    List<Patient> findPatientsByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
//    @Query("SELECT p FROM Patient p WHERE YEAR(CURRENT_DATE) - YEAR(p.dateOfBirth) BETWEEN :minAge AND :maxAge")
//    Page<Patient> findPatientsByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge, Pageable pageable);
//
//    // Check if patient exists by phone number
//    boolean existsByPhoneNumber(String phoneNumber);
//
//    // Check if patient exists by email
//    boolean existsByEmail(String email);
} 