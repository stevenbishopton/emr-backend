package hospital.emr.patient.exceptions;

public class MedicalHistoryNotFoundException extends RuntimeException {
    public MedicalHistoryNotFoundException(String message) {
        super(message);
    }
}
