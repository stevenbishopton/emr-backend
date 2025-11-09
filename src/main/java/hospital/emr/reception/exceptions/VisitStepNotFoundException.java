package hospital.emr.reception.exceptions;

public class VisitStepNotFoundException extends RuntimeException {
  public VisitStepNotFoundException(String message) {
    super(message);
  }
}
