package hospital.emr.reception.repos;

import com.rabbitmq.stream.ConsumerBuilder;
import hospital.emr.reception.entities.VisitDepartment;
import hospital.emr.reception.entities.VisitDepartmentId;
import hospital.emr.reception.enums.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
      and v.status = :visitStatus
    order by vd.assignedAt asc
""")
    List<VisitDepartment> findActiveQueueByDepartment(
            @Param("deptId") Long deptId,
            @Param("deptStatus") VisitStatus deptStatus,
            @Param("visitStatus") VisitStatus visitStatus
    );

}