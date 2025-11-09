package hospital.emr.common.repos;

import hospital.emr.common.entities.SimpleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SimpleRequestRepository extends JpaRepository<SimpleRequest, Long> {

}