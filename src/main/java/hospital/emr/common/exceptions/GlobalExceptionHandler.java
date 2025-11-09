package hospital.emr.common.exceptions;

import hospital.emr.bill.exceptions.BillNotFoundException;
import hospital.emr.patient.exceptions.PatientNotFoundException;
import hospital.emr.patient.exceptions.PrescriptionNotFoundException;
import hospital.emr.pharmacy.exceptions.ItemNotFoundException;
import hospital.emr.ward.exceptions.WardNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Specific handlers
    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ApiError> handlePatientNotFound(PatientNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(PrescriptionNotFoundException.class)
    public ResponseEntity<ApiError> handlePrescriptionNotFound(PrescriptionNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiError> handleItemNotFound(ItemNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BillNotFoundException.class)
    public ResponseEntity<ApiError> handleBillNotFound(BillNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(WardNotFoundException.class)
    public ResponseEntity<ApiError> handleWardNotFound(WardNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
//
//    @ExceptionHandler(ItemNotFoundException.class)
//    public ResponseEntity<ApiError> handleItemNotFound(ItemNotFoundException ex) {
//        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
//    }
//
//    @ExceptionHandler(ItemNotFoundException.class)
//    public ResponseEntity<ApiError> handleItemNotFound(ItemNotFoundException ex) {
//        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
//    }









    // Generic handler for uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
    }

    // Helper method
    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String message) {
        ApiError error = new ApiError(status.value(), message, LocalDateTime.now());
        return new ResponseEntity<>(error, status);
    }
}
