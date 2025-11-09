package hospital.emr.common.exceptions;

public class PersonnelNotFoundException extends RuntimeException {
    public PersonnelNotFoundException(String message) {
        super(message);
    }
}
