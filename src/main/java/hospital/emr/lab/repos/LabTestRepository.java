package hospital.emr.lab.repos;

import hospital.emr.lab.entities.LabTest;
import hospital.emr.lab.enums.SampleType;
import hospital.emr.lab.enums.TestCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for LabTest entities
 * Note: Since LabTest is a @MappedSuperclass, this repository works with all test subclasses
 * stored in the lab_tests table
 */
@Repository
public interface LabTestRepository extends JpaRepository<LabTest, Long> {

    /**
     * Find test by name (case-insensitive)
     */
    Optional<LabTest> findByNameIgnoreCase(String name);

    /**
     * Find all tests by category
     */
    @Query("SELECT t FROM LabTest t WHERE t.category = :category")
    List<LabTest> findByCategory(@Param("category") TestCategory category);

    /**
     * Find all tests by sample type
     */
    @Query("SELECT t FROM LabTest t WHERE t.sampleType = :sampleType")
    List<LabTest> findBySampleType(@Param("sampleType") SampleType sampleType);

    /**
     * Search tests by name (case-insensitive partial match)
     */
    @Query("SELECT t FROM LabTest t WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<LabTest> searchByName(@Param("searchTerm") String searchTerm);

    /**
     * Find tests by category and sample type
     */
    @Query("SELECT t FROM LabTest t WHERE t.category = :category AND t.sampleType = :sampleType")
    List<LabTest> findByCategoryAndSampleType(
        @Param("category") TestCategory category,
        @Param("sampleType") SampleType sampleType
    );

    /**
     * Check if test exists by name
     */
    boolean existsByNameIgnoreCase(String name);
}
