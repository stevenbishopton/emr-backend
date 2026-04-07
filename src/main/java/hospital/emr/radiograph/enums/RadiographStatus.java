package hospital.emr.radiograph.enums;

public enum RadiographStatus {
    REQUESTED("Requested"),
    SCHEDULED("Scheduled"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    REPORTED("Reported"),
    APPROVED("Approved");

    private final String displayName;

    RadiographStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
