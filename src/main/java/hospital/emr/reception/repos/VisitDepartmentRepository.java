package hospital.emr.reception.repos;

import hospital.emr.reception.entities.VisitDepartment;
import hospital.emr.reception.entities.VisitDepartmentId;
import hospital.emr.reception.enums.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitDepartmentRepository extends JpaRepository<VisitDepartment, VisitDepartmentId> {

    // Corrected method names
    List<VisitDepartment> findByIdDepartmentId(Long departmentId);

    List<VisitDepartment> findByIdVisitId(Long visitId);

    @Query("""
    select vd 
    from VisitDepartment vd 
    join fetch vd.visit v 
    where vd.department.id = :deptId 
      and vd.status = :deptStatus 
    order by vd.assignedAt asc
""")
    List<VisitDepartment> findActiveQueueByDepartment(
            @Param("deptId") Long deptId,
            @Param("deptStatus") VisitStatus deptStatus
    );

    @Query("""
    select vd 
    from VisitDepartment vd 
    join fetch vd.visit v 
    join fetch vd.department d
    where vd.department.id = :deptId 
      and vd.status = :deptStatus 
    order by vd.assignedAt asc
""")
    List<VisitDepartment> findDepartmentQueue(
            @Param("deptId") Long deptId,
            @Param("deptStatus") VisitStatus deptStatus
    );

    // NEW: Method for filtering with status only (no date filter)
    @Query("""
    select vd 
    from VisitDepartment vd 
    join fetch vd.visit v 
    join fetch v.patient p
    where vd.department.id = :deptId 
      and (:status is null or vd.status = :status)
    order by vd.assignedAt asc
""")
    List<VisitDepartment> findDepartmentQueueWithStatusFilter(
            @Param("deptId") Long deptId,
            @Param("status") VisitStatus status
    );

    // NEW: Method for filtering with date range only (no status filter)
    @Query("""
    select vd 
    from VisitDepartment vd 
    join fetch vd.visit v 
    join fetch v.patient p
    where vd.department.id = :deptId 
      and vd.assignedAt between :dateStart and :dateEnd
    order by vd.assignedAt asc
""")
    List<VisitDepartment> findDepartmentQueueWithDateFilter(
            @Param("deptId") Long deptId,
            @Param("dateStart") LocalDateTime dateStart,
            @Param("dateEnd") LocalDateTime dateEnd
    );

    // NEW: Method for filtering with both status and date range
    @Query("""
    select vd 
    from VisitDepartment vd 
    join fetch vd.visit v 
    join fetch v.patient p
    where vd.department.id = :deptId 
      and vd.status = :status
      and vd.assignedAt between :dateStart and :dateEnd
    order by vd.assignedAt asc
""")
    List<VisitDepartment> findDepartmentQueueWithStatusAndDateFilter(
            @Param("deptId") Long deptId,
            @Param("status") VisitStatus status,
            @Param("dateStart") LocalDateTime dateStart,
            @Param("dateEnd") LocalDateTime dateEnd
    );

    List<VisitDepartment> findByDepartmentIdAndStatus(Long departmentId, VisitStatus status);
}