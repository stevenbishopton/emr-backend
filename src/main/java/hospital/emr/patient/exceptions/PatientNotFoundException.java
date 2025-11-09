package hospital.emr.patient.exceptions;

public class PatientNotFoundException extends RuntimeException {
    public static final String MESSAGE = "Patient not found";

    public PatientNotFoundException() {
        super(MESSAGE);
    }

    // You can also keep a constructor for custom messages
    public PatientNotFoundException(String message) {
        super(message);
    }
}