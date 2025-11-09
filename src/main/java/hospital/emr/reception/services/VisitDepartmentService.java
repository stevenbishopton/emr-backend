// src/main/java/hospital/emr/reception/services/VisitDepartmentService.java
package hospital.emr.reception.services;

import hospital.emr.common.entities.Department;
import hospital.emr.common.exceptions.DepartmentNotFoundException;
import hospital.emr.common.repos.DepartmentRepository;
import hospital.emr.reception.dtos.VisitDepartmentDTO;
import hospital.emr.reception.entities.Visit;
import hospital.emr.reception.entities.VisitDepartment;
import hospital.emr.reception.entities.VisitDepartmentId;
import hospital.emr.reception.enums.VisitStatus;
import hospital.emr.reception.exceptions.VisitNotFoundException;
import hospital.emr.reception.mappers.VisitDepartmentMapper;
import hospital.emr.reception.repos.VisitDepartmentRepository;
import hospital.emr.reception.repos.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitDepartmentService {

    private final VisitRepository visitRepository;
    private final DepartmentRepository departmentRepository;
    private final VisitDepartmentRepository visitDepartmentRepository;
    private final VisitDepartmentMapper visitDepartmentMapper;

    @Transactional
    public VisitDepartmentDTO sendToDepartment(Long visitId, Long departmentId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found with ID: " + visitId));

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with ID: " + departmentId));

        VisitDepartmentId id = new VisitDepartmentId(visitId, departmentId);

        Optional<VisitDepartment> existingOpt = visitDepartmentRepository.findById(id);

        if (existingOpt.isPresent()) {
            VisitDepartment existing = existingOpt.get();
            // If the existing mapping is not IN_QUEUE, bring it back to IN_QUEUE (optional)
            if (existing.getStatus() != VisitStatus.IN_QUEUE) {
                existing.setStatus(VisitStatus.IN_QUEUE);
                existing.setAssignedAt(LocalDateTime.now());
                visitDepartmentRepository.save(existing);
            }
            return visitDepartmentMapper.toDto(existing);
        }

        VisitDepartment vd = new VisitDepartment();
        vd.setId(id);
        vd.setVisit(visit);
        vd.setDepartment(department);
        vd.setStatus(VisitStatus.IN_QUEUE);
        vd.setAssignedAt(LocalDateTime.now());

        VisitDepartment saved = visitDepartmentRepository.save(vd);
        return visitDepartmentMapper.toDto(saved);
    }


    @Transactional(readOnly = true)
    public List<VisitDepartmentDTO> findDepartmentsByVisitId(Long visitId) {
        return visitDepartmentRepository.findByIdVisitId(visitId).stream()
                .map(visitDepartmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitDepartmentDTO> findVisitsByDepartmentId(Long departmentId) {
        return visitDepartmentRepository.findByIdDepartmentId(departmentId).stream()
                .map(visitDepartmentMapper::toDto)
                .collect(Collectors.toList());
    }
}