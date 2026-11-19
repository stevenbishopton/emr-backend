package hospital.emr.common.audit;

import hospital.emr.security.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Records every mutating (POST/PUT/PATCH/DELETE) API call with the acting user
 * and outcome. Read-only requests are ignored to keep the log meaningful.
 */
@Component
@RequiredArgsConstructor
public class AuditInterceptor implements HandlerInterceptor {

    private static final String START_ATTR = "auditStart";
    private final AuditLogService auditLogService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute(START_ATTR, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        String method = request.getMethod();
        if (!isMutating(method)) {
            return;
        }

        Object started = request.getAttribute(START_ATTR);
        long duration = started instanceof Long ? System.currentTimeMillis() - (Long) started : 0L;

        AuditLog entry = AuditLog.builder()
                .actorId(SecurityUtils.currentUserId())
                .actorName(SecurityUtils.currentUserName())
                .method(method)
                .action(actionFor(method))
                .resource(resourceFor(request.getRequestURI()))
                .path(request.getRequestURI())
                .status(response.getStatus())
                .ipAddress(clientIp(request))
                .detail(request.getQueryString())
                .durationMs(duration)
                .build();

        auditLogService.record(entry);
    }

    private boolean isMutating(String method) {
        return "POST".equals(method) || "PUT".equals(method)
                || "PATCH".equals(method) || "DELETE".equals(method);
    }

    private String actionFor(String method) {
        return switch (method) {
            case "POST" -> "CREATE";
            case "PUT", "PATCH" -> "UPDATE";
            case "DELETE" -> "DELETE";
            default -> method;
        };
    }

    private String resourceFor(String uri) {
        if (uri == null) return null;
        String path = uri.startsWith("/emr/") ? uri.substring(5) : uri.replaceFirst("^/", "");
        if (path.isBlank()) return null;
        String[] parts = path.split("/");
        return parts.length > 0 ? parts[0] : null;
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
