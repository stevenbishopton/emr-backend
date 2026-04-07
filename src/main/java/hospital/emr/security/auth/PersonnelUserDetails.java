package hospital.emr.security.auth;

import hospital.emr.common.entities.Personnel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class PersonnelUserDetails implements UserDetails {

    private final Personnel personnel;
    private final Collection<? extends GrantedAuthority> authorities;

    public PersonnelUserDetails(Personnel personnel, Collection<? extends GrantedAuthority> authorities) {
        this.personnel = personnel;
        this.authorities = authorities;
    }

    public Personnel getPersonnel() {
        return personnel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return personnel.getPassword();
    }

    @Override
    public String getUsername() {
        return personnel.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
