package hospital.emr.common.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    /** Persist an audit entry. Never let auditing break the actual request. */
    public void record(AuditLog entry) {
        try {
            auditLogRepository.save(entry);
        } catch (Exception ex) {
            log.warn("Failed to persist audit entry for {} {}: {}",
                    entry.getMethod(), entry.getPath(), ex.getMessage());
        }
    }

    public Page<AuditLogDTO> search(String actorName, String action, String resource,
                                    LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return auditLogRepository.search(
                blankToNull(actorName), blankToNull(action), blankToNull(resource),
                from, to, pageable).map(this::toDto);
    }

    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;
    }

    private AuditLogDTO toDto(AuditLog entity) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(entity.getId());
        dto.setActorId(entity.getActorId());
        dto.setActorName(entity.getActorName());
        dto.setMethod(entity.getMethod());
        dto.setAction(entity.getAction());
        dto.setResource(entity.getResource());
        dto.setPath(entity.getPath());
        dto.setStatus(entity.getStatus());
        dto.setIpAddress(entity.getIpAddress());
        dto.setDetail(entity.getDetail());
        dto.setDurationMs(entity.getDurationMs());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
