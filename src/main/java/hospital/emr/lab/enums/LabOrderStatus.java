package hospital.emr.lab.enums;

import java.util.EnumSet;
import java.util.Set;

public enum LabOrderStatus {
    ORDERED,
    COLLECTED,
    IN_LAB,
    RESULTED,
    VERIFIED,
    CANCELLED;

    public Set<LabOrderStatus> allowedNext() {
        return switch (this) {
            case ORDERED -> EnumSet.of(COLLECTED, CANCELLED);
            case COLLECTED -> EnumSet.of(IN_LAB, CANCELLED);
            case IN_LAB -> EnumSet.of(RESULTED, CANCELLED);
            case RESULTED -> EnumSet.of(VERIFIED, IN_LAB, CANCELLED);
            case VERIFIED, CANCELLED -> EnumSet.noneOf(LabOrderStatus.class);
        };
    }

    public boolean canTransitionTo(LabOrderStatus target) {
        return target != null && allowedNext().contains(target);
    }
}
