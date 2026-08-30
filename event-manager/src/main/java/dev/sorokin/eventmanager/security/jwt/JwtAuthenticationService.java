package dev.sorokin.eventmanager.security.jwt;

import dev.sorokin.eventmanager.dto.SignInDto;
import dev.sorokin.eventmanager.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenManager jwtTokenManager;
    private final UserRepository userRepository;

    public JwtAuthenticationService(AuthenticationManager authenticationManager, AuthenticationProvider authenticationProvider, JwtTokenManager jwtTokenManager, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenManager = jwtTokenManager;
        this.userRepository = userRepository;
    }

    public String authenticateUser(SignInDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getLogin(),
                        dto.getPassword()
                )
        );
        var user = userRepository.findByLogin(dto.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + dto.getLogin() + " not found"));
        return jwtTokenManager.generateToken(user.getLogin(), user.getId(), user.getRole());
    }
}
