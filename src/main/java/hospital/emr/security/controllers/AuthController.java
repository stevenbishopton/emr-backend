package hospital.emr.security.controllers;

import hospital.emr.common.dtos.PersonnelDTO;
import hospital.emr.common.services.PersonnelService;
import hospital.emr.security.auth.PersonnelUserDetails;
import hospital.emr.security.dto.AuthResponse;
import hospital.emr.security.dto.LoginRequest;
import hospital.emr.security.jwt.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/emr/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PersonnelService personnelService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<PersonnelDTO> register(@Valid @RequestBody PersonnelDTO request) {
        PersonnelDTO created = personnelService.createPersonnel(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        PersonnelUserDetails principal = (PersonnelUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(principal);
        Instant expiresAt = Instant.now().plusMillis(jwtService.getExpirationMillis());
        List<String> roles = principal.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        AuthResponse response = new AuthResponse(
                principal.getPersonnel().getId(),
                principal.getUsername(),
                roles,
                token,
                expiresAt,
                "Login successful"
        );

        return ResponseEntity.ok(response);
    }
}
