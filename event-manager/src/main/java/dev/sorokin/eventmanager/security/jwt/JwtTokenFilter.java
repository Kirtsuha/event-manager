package dev.sorokin.eventmanager.security.jwt;

import dev.sorokin.eventmanager.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;

    public JwtTokenFilter(JwtTokenManager jwtTokenManager, UserService userService) {
        this.jwtTokenManager = jwtTokenManager;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("=== JWT FILTER ===");
        System.out.println("URI: " + request.getRequestURI());

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("Authorization header: " + header);

        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("No Bearer token found");
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = header.substring(7);
        System.out.println("Token: " + jwtToken);

        try {
            String loginFromToken = jwtTokenManager.getLoginFromToken(jwtToken);
            System.out.println("Login from token: " + loginFromToken);

            if (loginFromToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                var user = userService.findByLogin(loginFromToken);
                System.out.println("User found: " + (user != null));

                if (user != null) {
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                            user.getLogin(),
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                    );

                    SecurityContextHolder.getContext().setAuthentication(token);
                    System.out.println("Authentication set successfully");
                    System.out.println("Authorities: " + token.getAuthorities());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
