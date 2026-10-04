package hospital.emr.security;

import hospital.emr.common.entities.Personnel;
import hospital.emr.security.auth.PersonnelUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Convenience accessors for the authenticated principal so services can record
 * real actors instead of hardcoded ids.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<PersonnelUserDetails> currentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof PersonnelUserDetails details) {
            return Optional.of(details);
        }
        return Optional.empty();
    }

    public static Optional<Personnel> currentPersonnel() {
        return currentUserDetails().map(PersonnelUserDetails::getPersonnel);
    }

    public static Long currentUserId() {
        return currentPersonnel().map(Personnel::getId).orElse(null);
    }

    public static String currentUserName() {
        return currentPersonnel().map(Personnel::getNames).orElse(null);
    }

    public static boolean hasRole(String role) {
        return currentUserDetails()
                .map(details -> details.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals(role)))
                .orElse(false);
    }
}
