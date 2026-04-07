package hospital.emr.radiograph.services;

import hospital.emr.radiograph.dtos.RadiographHistoryDTO;
import hospital.emr.radiograph.enums.RadiographStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface RadiographHistoryService {
    
    List<RadiographHistoryDTO> getRadiographHistory(Long radiographId);
    
    RadiographHistoryDTO addHistoryEntry(RadiographHistoryDTO dto);
    
    void updateRadiographStatus(Long radiographId, RadiographStatus newStatus, 
                               Long performedBy, String notes);
    
    void updateRadiographStatus(Long radiographId, RadiographStatus newStatus, 
                               Long performedBy, String notes, String reason);
    
    List<RadiographHistoryDTO> getHistoryByDateRange(Long radiographId, 
                                                     LocalDateTime start, LocalDateTime end);
    
    List<RadiographHistoryDTO> getHistoryByPerformedBy(Long userId);
    
    void deleteHistoryEntry(Long historyId);
    
    boolean hasHistoryEntry(Long radiographId, RadiographStatus status);
}
