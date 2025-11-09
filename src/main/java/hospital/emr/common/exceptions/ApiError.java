package hospital.emr.common.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public ApiError(int value, String message, LocalDateTime now) {
    }
}
