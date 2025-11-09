package hospital.emr.pharmacy.repos;

import hospital.emr.pharmacy.entities.Pharmacist;
import hospital.emr.common.enums.Sex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {
    
    // Find pharmacist by username
    Optional<Pharmacist> findByUsername(String username);
    
    // Find pharmacist by email
    Optional<Pharmacist> findByEmail(String email);
    
    // Find pharmacist by phone number
    Optional<Pharmacist> findByPhoneNumber(String phoneNumber);
    
    // Find pharmacists by name (partial match)
    List<Pharmacist> findByNamesContainingIgnoreCase(String names);
    
    // Find pharmacists by sex
    List<Pharmacist> findBySex(Sex sex);
    
    // Find pharmacists by address (partial match)
    @Query("SELECT p FROM Pharmacist p WHERE p.address LIKE %:address%")
    List<Pharmacist> findByAddressContaining(@Param("address") String address);
    
    // Find pharmacists by username and password (for authentication)
    Optional<Pharmacist> findByUsernameAndPassword(String username, String password);
    
    // Find pharmacists with email
    @Query("SELECT p FROM Pharmacist p WHERE p.email IS NOT NULL")
    List<Pharmacist> findPharmacistsWithEmail();
    
    // Find pharmacists without email
    @Query("SELECT p FROM Pharmacist p WHERE p.email IS NULL")
    List<Pharmacist> findPharmacistsWithoutEmail();
    
    // Find pharmacists with address
    @Query("SELECT p FROM Pharmacist p WHERE p.address IS NOT NULL")
    List<Pharmacist> findPharmacistsWithAddress();
    
    // Find pharmacists without address
    @Query("SELECT p FROM Pharmacist p WHERE p.address IS NULL")
    List<Pharmacist> findPharmacistsWithoutAddress();
    
    // Check if pharmacist exists by username
    boolean existsByUsername(String username);
    
    // Check if pharmacist exists by email
    boolean existsByEmail(String email);
    
    // Check if pharmacist exists by phone number
    boolean existsByPhoneNumber(String phoneNumber);
    
    // Count pharmacists by sex
    long countBySex(Sex sex);
    
    // Count pharmacists with email
    @Query("SELECT COUNT(p) FROM Pharmacist p WHERE p.email IS NOT NULL")
    long countPharmacistsWithEmail();
} 