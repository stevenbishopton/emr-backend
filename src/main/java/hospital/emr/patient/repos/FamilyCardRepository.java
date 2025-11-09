package hospital.emr.patient.repos;

import hospital.emr.patient.entities.FamilyCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyCardRepository extends JpaRepository<FamilyCard, Long> {
    
//    // Find family card by name
//    Optional<FamilyCard> findByNames(String names);
//
//    // Find family cards by name (partial match)
//    List<FamilyCard> findByNamesContainingIgnoreCase(String names);
//
//    // Find family cards with members
//    @Query("SELECT fc FROM FamilyCard fc WHERE fc.members IS NOT EMPTY")
//    List<FamilyCard> findFamilyCardsWithMembers();
//
//    // Find family cards without members
//    @Query("SELECT fc FROM FamilyCard fc WHERE fc.members IS EMPTY")
//    List<FamilyCard> findFamilyCardsWithoutMembers();
//
//    // Find family cards by member count
//    @Query("SELECT fc FROM FamilyCard fc WHERE SIZE(fc.members) = :memberCount")
//    List<FamilyCard> findByMemberCount(@Param("memberCount") int memberCount);
//
//    // Find family cards with more than specified member count
//    @Query("SELECT fc FROM FamilyCard fc WHERE SIZE(fc.members) > :memberCount")
//    List<FamilyCard> findByMemberCountGreaterThan(@Param("memberCount") int memberCount);
//
//    // Find family cards with less than specified member count
//    @Query("SELECT fc FROM FamilyCard fc WHERE SIZE(fc.members) < :memberCount")
//    List<FamilyCard> findByMemberCountLessThan(@Param("memberCount") int memberCount);
//
//    // Count family cards with members
//    @Query("SELECT COUNT(fc) FROM FamilyCard fc WHERE fc.members IS NOT EMPTY")
//    long countFamilyCardsWithMembers();
//
//    // Count family cards without members
//    @Query("SELECT COUNT(fc) FROM FamilyCard fc WHERE fc.members IS EMPTY")
//    long countFamilyCardsWithoutMembers();
} 