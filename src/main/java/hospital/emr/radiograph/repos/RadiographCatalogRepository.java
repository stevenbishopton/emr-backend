package hospital.emr.radiograph.repos;

import hospital.emr.radiograph.entities.RadiographCatalog;
import hospital.emr.radiograph.enums.RadiographType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RadiographCatalogRepository extends JpaRepository<RadiographCatalog, Long> {
    
    Optional<RadiographCatalog> findByName(String name);
    
    List<RadiographCatalog> findByType(RadiographType type);
    
    List<RadiographCatalog> findByActiveTrue();
    
    List<RadiographCatalog> findByActiveFalse();
    
    @Query("SELECT r FROM RadiographCatalog r WHERE r.active = true ORDER BY r.name")
    List<RadiographCatalog> findActiveCatalogItemsOrdered();
    
    @Query("SELECT r FROM RadiographCatalog r WHERE r.name LIKE %:name% AND r.active = true")
    List<RadiographCatalog> findActiveCatalogItemsByNameContaining(@Param("name") String name);
    
    boolean existsByName(String name);
}
