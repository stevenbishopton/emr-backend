package hospital.emr.radiograph.services;

import hospital.emr.radiograph.dtos.RadiographVisitHistoryDTO;
import hospital.emr.radiograph.entities.RadiographVisitHistory;
import hospital.emr.radiograph.enums.RadiographStatus;
import hospital.emr.radiograph.mappers.RadiographVisitHistoryMapper;
import hospital.emr.radiograph.repos.RadiographVisitHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RadiographVisitHistoryServiceImpl implements RadiographVisitHistoryService {

    private final RadiographVisitHistoryRepository repository;
    private final RadiographVisitHistoryMapper mapper;

    @Override
    @Transactional
    public RadiographVisitHistoryDTO createVisitHistory(RadiographVisitHistoryDTO dto) {
        log.info("Creating radiograph visit history for patient: {}", dto.getPatient().getFullName());
        RadiographVisitHistory entity = mapper.createEntity(dto);
        entity.setVisitDate(LocalDateTime.now());
        RadiographVisitHistory saved = repository.save(entity);
        log.info("Successfully created radiograph visit history with ID: {}", saved.getId());
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public RadiographVisitHistoryDTO updateVisitHistory(Long id, RadiographVisitHistoryDTO dto) {
        log.info("Updating radiograph visit history with ID: {}", id);
        RadiographVisitHistory existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Radiograph visit history not found with ID: " + id));
        
        mapper.updateEntityFromDto(dto, existing);
        RadiographVisitHistory updated = repository.save(existing);
        log.info("Successfully updated radiograph visit history with ID: {}", updated.getId());
        return mapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RadiographVisitHistoryDTO> getVisitHistoryById(Long id) {
        log.debug("Fetching radiograph visit history with ID: {}", id);
        return repository.findById(id)
                .map(mapper::toDto);
    }

    @Override
    @Transactional
    public void deleteVisitHistory(Long id) {
        log.info("Deleting radiograph visit history with ID: {}", id);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Radiograph visit history not found with ID: " + id);
        }
        repository.deleteById(id);
        log.info("Successfully deleted radiograph visit history with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getAllVisitHistories() {
        log.debug("Fetching all radiograph visit histories");
        List<RadiographVisitHistory> histories = repository.findAll();
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RadiographVisitHistoryDTO> getAllVisitHistories(Pageable pageable) {
        log.debug("Fetching all radiograph visit histories with pagination");
        Page<RadiographVisitHistory> page = repository.findAll(pageable);
        return page.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getPatientRadiographHistory(Long patientId) {
        log.debug("Fetching radiograph history for patient ID: {}", patientId);
        List<RadiographVisitHistory> histories = repository.findByPatientIdOrderByVisitDateDesc(patientId);
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RadiographVisitHistoryDTO> getPatientRadiographHistory(Long patientId, Pageable pageable) {
        log.debug("Fetching radiograph history for patient ID: {} with pagination", patientId);
        Page<RadiographVisitHistory> page = repository.findByPatientIdOrderByVisitDateDesc(patientId, pageable);
        return page.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getPatientRadiographHistoryByDateRange(Long patientId, LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching radiograph history for patient ID: {} between {} and {}", patientId, startDate, endDate);
        List<RadiographVisitHistory> histories = repository.findByPatientIdAndVisitDateBetween(patientId, startDate, endDate);
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RadiographVisitHistoryDTO> getVisitHistoryByVisitId(Long visitId) {
        log.debug("Fetching radiograph visit history for visit ID: {}", visitId);
        return repository.findByVisitId(visitId)
                .map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getVisitHistoriesByStatus(RadiographStatus status) {
        log.debug("Fetching radiograph visit histories with status: {}", status);
        List<RadiographVisitHistory> histories = repository.findByStatusOrderByVisitDateDesc(status);
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RadiographVisitHistoryDTO> getVisitHistoriesByStatus(RadiographStatus status, Pageable pageable) {
        log.debug("Fetching radiograph visit histories with status: {} with pagination", status);
        Page<RadiographVisitHistory> page = repository.findByStatusOrderByVisitDateDesc(status, pageable);
        return page.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getVisitsRequiringAttention() {
        log.debug("Fetching radiograph visits requiring attention");
        List<RadiographVisitHistory> histories = repository.findVisitsRequiringAttention();
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getCompletedVisitsWithReports() {
        log.debug("Fetching completed radiograph visits with reports");
        List<RadiographVisitHistory> histories = repository.findCompletedVisitsWithReports();
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getVisitHistoriesByRequestedBy(Long requestedById) {
        log.debug("Fetching radiograph visit histories requested by personnel ID: {}", requestedById);
        List<RadiographVisitHistory> histories = repository.findByRequestedByIdOrderByVisitDateDesc(requestedById);
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getVisitHistoriesByPerformedBy(Long performedById) {
        log.debug("Fetching radiograph visit histories performed by personnel ID: {}", performedById);
        List<RadiographVisitHistory> histories = repository.findByPerformedByIdOrderByVisitDateDesc(performedById);
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getVisitHistoriesByRadiologist(Long radiologistId) {
        log.debug("Fetching radiograph visit histories for radiologist ID: {}", radiologistId);
        List<RadiographVisitHistory> histories = repository.findByRadiologistIdOrderByVisitDateDesc(radiologistId);
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getVisitHistoriesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.debug("Fetching radiograph visit histories between {} and {}", startDate, endDate);
        List<RadiographVisitHistory> histories = repository.findByVisitDateBetween(startDate, endDate);
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RadiographVisitHistoryDTO> getVisitHistoriesByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.debug("Fetching radiograph visit histories between {} and {} with pagination", startDate, endDate);
        Page<RadiographVisitHistory> page = repository.findByVisitDateBetweenOrderByVisitDateDesc(startDate, endDate, pageable);
        return page.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getRecentVisits(int days) {
        log.debug("Fetching radiograph visits from last {} days", days);
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<RadiographVisitHistory> histories = repository.findRecentVisits(since);
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> searchByPatient(String searchTerm) {
        log.debug("Searching radiograph visit histories by patient term: {}", searchTerm);
        List<RadiographVisitHistory> histories = repository.searchByPatient(searchTerm);
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RadiographVisitHistoryDTO> searchByPatient(String searchTerm, Pageable pageable) {
        log.debug("Searching radiograph visit histories by patient term: {} with pagination", searchTerm);
        Page<RadiographVisitHistory> page = repository.searchByPatient(searchTerm, pageable);
        return page.map(mapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographVisitHistoryDTO> getVisitHistoriesByVisitType(String visitType) {
        log.debug("Fetching radiograph visit histories by visit type: {}", visitType);
        List<RadiographVisitHistory> histories = repository.findByVisitTypeOrderByVisitDateDesc(visitType);
        return mapper.toDtoList(histories);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByStatus(RadiographStatus status) {
        log.debug("Counting radiograph visit histories by status: {}", status);
        return repository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getVisitStatistics() {
        log.debug("Fetching radiograph visit statistics");
        // This would need to be implemented in the repository
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        history -> history.getStatus(),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> new Object[]{entry.getKey(), entry.getValue()})
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RadiographVisitHistoryDTO updateStatus(Long id, RadiographStatus newStatus, Long performedById, String notes) {
        log.info("Updating status for radiograph visit history ID: {} to {}", id, newStatus);
        RadiographVisitHistory entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Radiograph visit history not found with ID: " + id));
        
        entity.setStatus(newStatus);
        if (newStatus == RadiographStatus.COMPLETED) {
            entity.setCompletedDate(LocalDateTime.now());
        }
        
        RadiographVisitHistory updated = repository.save(entity);
        log.info("Successfully updated status for radiograph visit history ID: {}", updated.getId());
        return mapper.toDto(updated);
    }

    @Override
    @Transactional
    public RadiographVisitHistoryDTO addRadiologistReport(Long id, String report, Long radiologistId) {
        log.info("Adding radiologist report for radiograph visit history ID: {}", id);
        RadiographVisitHistory entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Radiograph visit history not found with ID: " + id));
        
        entity.setRadiologistReport(report);
        entity.setStatus(RadiographStatus.COMPLETED);
        entity.setCompletedDate(LocalDateTime.now());
        
        RadiographVisitHistory updated = repository.save(entity);
        log.info("Successfully added radiologist report for radiograph visit history ID: {}", updated.getId());
        return mapper.toDto(updated);
    }

    @Override
    @Transactional
    public RadiographVisitHistoryDTO addTechnicianNotes(Long id, String notes, Long technicianId) {
        log.info("Adding technician notes for radiograph visit history ID: {}", id);
        RadiographVisitHistory entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Radiograph visit history not found with ID: " + id));
        
        entity.setTechnicianNotes(notes);
        
        RadiographVisitHistory updated = repository.save(entity);
        log.info("Successfully added technician notes for radiograph visit history ID: {}", updated.getId());
        return mapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByVisitId(Long visitId) {
        return repository.findByVisitId(visitId).isPresent();
    }
}
