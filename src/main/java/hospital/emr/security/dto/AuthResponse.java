package hospital.emr.security.dto;

import java.time.Instant;
import java.util.List;

public record AuthResponse(
        Long personnelId,
        String username,
        List<String> roles,
        String token,
        Instant expiresAt,
        String message
) {
}
