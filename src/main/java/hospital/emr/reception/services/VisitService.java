// src/main/java/hospital/emr/reception/services/VisitService.java
package hospital.emr.reception.services;

import hospital.emr.common.entities.Department;
import hospital.emr.common.exceptions.DepartmentNotFoundException;
import hospital.emr.common.repos.DepartmentRepository;
import hospital.emr.patient.entities.Patient;
import hospital.emr.patient.exceptions.PatientNotFoundException;
import hospital.emr.patient.repos.PatientRepository;
import hospital.emr.reception.dtos.NewVisitRequest;
import hospital.emr.reception.dtos.VisitDTO;
import hospital.emr.reception.entities.Visit;
import hospital.emr.reception.entities.VisitDepartment;
import hospital.emr.reception.entities.VisitDepartmentId;
import hospital.emr.reception.enums.VisitStatus;
import hospital.emr.reception.exceptions.VisitNotFoundException;
import hospital.emr.reception.mappers.VisitMapper;
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
public class VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;
    private final PatientRepository patientRepository;
    private final VisitDepartmentRepository visitDepartmentRepository;
    private final DepartmentRepository departmentRepository;
    private final VisitDepartmentService visitDepartmentService;

    @Transactional
    public VisitDTO createVisitAndSendToDepartment(NewVisitRequest request) {
        // 1. Find the patient
        Patient patient = patientRepository.getReferenceById(request.getPatientId());

        // 2. Create and save the Visit entity
        Visit visit = visitMapper.toEntity(request);
        visit.setPatient(patient);
        Visit savedVisit = visitRepository.save(visit);

        // 3. Find the department
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with ID: " + request.getDepartmentId()));

        // 4. Create and save the VisitDepartment entity (association)
        VisitDepartmentId visitDepartmentId = new VisitDepartmentId(savedVisit.getId(), department.getId());
        VisitDepartment visitDepartment = new VisitDepartment();
        visitDepartment.setId(visitDepartmentId);
        visitDepartment.setVisit(savedVisit);
        visitDepartment.setDepartment(department);
        visitDepartment.setStatus(VisitStatus.IN_QUEUE);
        visitDepartment.setAssignedAt(LocalDateTime.now());
        visitDepartmentRepository.save(visitDepartment);

        return visitMapper.toDto(savedVisit);
    }

    @Transactional
    public VisitDTO createVisit(NewVisitRequest request) {
        // Find the patient using getReferenceById to get a proxy, avoiding an unnecessary database hit.
        Patient patient = patientRepository.getReferenceById(request.getPatientId());
        Visit visit = visitMapper.toEntity(request);
        visit.setPatient(patient);

        Visit savedVisit = visitRepository.save(visit);
        return visitMapper.toDto(savedVisit);
    }

    @Transactional(readOnly = true)
    public VisitDTO findVisitById(Long id) {
        return visitRepository.findById(id)
                .map(visitMapper::toDto)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found with ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<VisitDTO> findAllVisits() {
        return visitRepository.findAll().stream()
                .map(visitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitDTO> findVisitsByPatientId(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with ID: " + patientId);
        }
        return visitRepository.findByPatientId(patientId).stream()
                .map(visitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public VisitDTO markVisitAsCompleted(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found with ID: " + visitId));
        visit.setStatus(VisitStatus.COMPLETED);
        Visit updatedVisit = visitRepository.save(visit);

        return visitMapper.toDto(updatedVisit);
    }

    @Transactional
    public VisitDTO makeVisitAdmission(Long visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found with ID: " + visitId));
        visit.setStatus(VisitStatus.ADMITTED);
        Visit updatedVisit = visitRepository.save(visit);

        return visitMapper.toDto(updatedVisit);
    }

    @Transactional(readOnly = true)
    public List<VisitDTO> findInQueue(){
        return visitRepository.findByStatus(VisitStatus.IN_QUEUE).stream()
                .map(visitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitDTO> findAdmitted(){
        return visitRepository.findByStatus(VisitStatus.ADMITTED).stream()
                .map(visitMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisitDTO> findVisitsInQueueByDepartment(Long deptId) {
        return visitDepartmentRepository
                .findActiveQueueByDepartment(deptId, VisitStatus.IN_QUEUE, VisitStatus.IN_QUEUE)
                .stream()
                .map(VisitDepartment::getVisit)  // visits are already filtered at query level
                .distinct()                      // still safe if multiple VisitDepartments map to same visit
                .map(visitMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteVisit(Long id) {
        if (!visitRepository.existsById(id)) {
            throw new VisitNotFoundException("Visit not found with ID: " + id);
        }
        visitRepository.deleteById(id);
    }
}