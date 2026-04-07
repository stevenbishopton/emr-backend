package hospital.emr.security.auth;

import hospital.emr.common.entities.Personnel;
import jakarta.persistence.DiscriminatorValue;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonnelAuthoritiesMapper {

    private static final String ROLE_PREFIX = "ROLE_";

    public List<GrantedAuthority> mapToAuthorities(Personnel personnel) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        resolveRoleName(personnel)
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role))
                .ifPresent(authorities::add);

        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + "STAFF"));

        return authorities;
    }

    private java.util.Optional<String> resolveRoleName(Personnel personnel) {
        if (personnel == null) {
            return java.util.Optional.empty();
        }

        DiscriminatorValue discriminatorValue = personnel.getClass().getAnnotation(DiscriminatorValue.class);
        if (discriminatorValue != null && !discriminatorValue.value().isBlank()) {
            return java.util.Optional.of(discriminatorValue.value().toUpperCase());
        }

        return java.util.Optional.of(personnel.getClass().getSimpleName().toUpperCase());
    }
}
