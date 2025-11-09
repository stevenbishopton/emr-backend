package hospital.emr.patient.exceptions;

public class PrescriptionNotFoundException extends RuntimeException {
    public static final String MESSAGE = "Prescription not found";

    public PrescriptionNotFoundException() {
        super(MESSAGE);
    }

    // You can also keep a constructor for custom messages
    public PrescriptionNotFoundException(String message) {
        super(message);
    }
}
