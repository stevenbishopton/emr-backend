package hospital.emr.common.audit;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditLogDTO {
    private Long id;
    private Long actorId;
    private String actorName;
    private String method;
    private String action;
    private String resource;
    private String path;
    private Integer status;
    private String ipAddress;
    private String detail;
    private Long durationMs;
    private LocalDateTime createdAt;
}
