package hospital.emr.security.auth;

import hospital.emr.common.entities.Personnel;
import hospital.emr.common.repos.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonnelUserDetailsService implements UserDetailsService {

    private final PersonnelRepository personnelRepository;
    private final PersonnelAuthoritiesMapper authoritiesMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Personnel personnel = personnelRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Personnel not found for username: " + username));

        return new PersonnelUserDetails(personnel, authoritiesMapper.mapToAuthorities(personnel));
    }
}
