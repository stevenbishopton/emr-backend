package hospital.emr.radiograph.services;

import hospital.emr.radiograph.dtos.RadiographHistoryDTO;
import hospital.emr.radiograph.entities.RadiographHistory;
import hospital.emr.radiograph.enums.RadiographStatus;
import hospital.emr.radiograph.mappers.RadiographHistoryMapper;
import hospital.emr.radiograph.repos.RadiographHistoryRepository;
import hospital.emr.radiograph.repos.RadiographRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RadiographHistoryServiceImpl implements RadiographHistoryService {

    private final RadiographHistoryRepository historyRepository;
    private final RadiographRepository radiographRepository;
    private final RadiographHistoryMapper historyMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RadiographHistoryDTO> getRadiographHistory(Long radiographId) {
        log.debug("Fetching history for radiograph: {}", radiographId);
        List<RadiographHistory> history = historyRepository.findByRadiographIdOrderByTimestampDesc(radiographId);
        return history.stream()
                .map(historyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RadiographHistoryDTO addHistoryEntry(RadiographHistoryDTO dto) {
        log.info("Adding history entry for radiograph: {}", dto.getRadiographId());
        
        RadiographHistory entity = historyMapper.toEntity(dto);
        entity.setTimestamp(LocalDateTime.now());
        
        RadiographHistory saved = historyRepository.save(entity);
        log.info("Added history entry with id: {}", saved.getId());
        
        return historyMapper.toDto(saved);
    }

    @Override
    public void updateRadiographStatus(Long radiographId, RadiographStatus newStatus, 
                                   Long performedBy, String notes) {
        updateRadiographStatus(radiographId, newStatus, performedBy, notes, "Status update");
    }

    @Override
    public void updateRadiographStatus(Long radiographId, RadiographStatus newStatus, 
                                   Long performedBy, String notes, String reason) {
        log.info("Updating radiograph {} status to {} by user {}", radiographId, newStatus, performedBy);
        
        // Get current radiograph to capture previous status
        radiographRepository.findById(radiographId).ifPresent(radiograph -> {
            RadiographStatus previousStatus = radiograph.getStatus();
            
            // Update radiograph status
            radiograph.setStatus(newStatus);
            radiographRepository.save(radiograph);
            
            // Create history entry
            RadiographHistory historyEntry = new RadiographHistory();
            historyEntry.setRadiographId(radiographId);
            historyEntry.setStatus(newStatus);
            historyEntry.setPreviousStatus(previousStatus.name());
            historyEntry.setNotes(notes);
            historyEntry.setReason(reason);
            historyEntry.setPerformedBy(performedBy);
            historyEntry.setTimestamp(LocalDateTime.now());
            
            historyRepository.save(historyEntry);
            
            log.info("Updated radiograph {} status from {} to {}", radiographId, previousStatus, newStatus);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographHistoryDTO> getHistoryByDateRange(Long radiographId, 
                                                             LocalDateTime start, LocalDateTime end) {
        log.debug("Fetching history for radiograph {} between {} and {}", radiographId, start, end);
        List<RadiographHistory> history = historyRepository.findByRadiographIdAndDateRange(radiographId, start, end);
        return history.stream()
                .map(historyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RadiographHistoryDTO> getHistoryByPerformedBy(Long userId) {
        log.debug("Fetching history performed by user: {}", userId);
        List<RadiographHistory> history = historyRepository.findByPerformedBy(userId);
        return history.stream()
                .map(historyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteHistoryEntry(Long historyId) {
        log.info("Deleting history entry: {}", historyId);
        historyRepository.deleteById(historyId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasHistoryEntry(Long radiographId, RadiographStatus status) {
        List<RadiographHistory> entries = historyRepository.findByRadiographIdAndStatus(radiographId, status);
        return !entries.isEmpty();
    }
}
