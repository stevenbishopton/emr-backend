package hospital.emr.pharmacy.repos;

import hospital.emr.pharmacy.entities.Item;
import hospital.emr.pharmacy.enums.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    
    // Find item by name
    Optional<Item> findByName(String name);
    
    // Find items by name (partial match)
    Page<Item> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Find items by type
    List<Item> findByItemType(ItemType itemType);
    
    // Find items by expiration date
    List<Item> findByExpirationDate(LocalDate expirationDate);
    
    // Find items expiring before a specific date
    List<Item> findByExpirationDateBefore(LocalDate date);
    
    // Find items expiring between dates
    List<Item> findByExpirationDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Find items with low stock (quantity less than specified)
    
    // Find items with zero stock
    @Query("SELECT i FROM Item i WHERE i.quantity = 0")
    List<Item> findItemsWithZeroStock();
    
    // Find items by cost price range
    @Query("SELECT i FROM Item i WHERE i.costPrice BETWEEN :minPrice AND :maxPrice")
    List<Item> findByCostPriceBetween(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    // Find items by selling price range
    @Query("SELECT i FROM Item i WHERE i.sellingPrice BETWEEN :minPrice AND :maxPrice")
    List<Item> findBySellingPriceBetween(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    // Find items by description (partial match)
    @Query("SELECT i FROM Item i WHERE i.description LIKE %:description%")
    List<Item> findByDescriptionContaining(@Param("description") String description);
    
    // Find items by type and name
    List<Item> findByItemTypeAndNameContainingIgnoreCase(ItemType itemType, String name);
    
    // Find items expiring soon (within 30 days)
    @Query("SELECT i FROM Item i WHERE i.expirationDate BETWEEN CURRENT_DATE AND :futureDate")
    List<Item> findItemsExpiringSoon(@Param("futureDate") LocalDate futureDate);
    
    // Find items with high profit margin
    @Query("SELECT i FROM Item i WHERE (i.sellingPrice - i.costPrice) / i.costPrice > :margin")
    List<Item> findItemsWithHighProfitMargin(@Param("margin") BigDecimal margin);
    
    // Count items by type
    long countByItemType(ItemType itemType);
    
    // Count items with low stock
    @Query("SELECT COUNT(i) FROM Item i WHERE i.quantity < :threshold")
    long countItemsWithLowStock(@Param("threshold") BigDecimal threshold);
    
    // Count expired items
    @Query("SELECT COUNT(i) FROM Item i WHERE i.expirationDate < CURRENT_DATE")
    long countExpiredItems();
    
    // Check if item exists by name
    boolean existsByName(String name);
} 