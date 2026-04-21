package dev.sorokin.eventmanager.security;

import dev.sorokin.eventmanager.security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/webjars/**",
                                        "/swagger-resources/**",
                                        "/openapi.yaml"
                                ).permitAll()

                                .requestMatchers(HttpMethod.GET, "/users/**")
                                .hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/users/**")
                                .permitAll()

                                .requestMatchers(HttpMethod.GET, "/locations/**")
                                .hasAnyAuthority("ADMIN", "USER", "ROLE_ADMIN", "ROLE_USER")
                                .requestMatchers(HttpMethod.POST, "/locations")
                                .hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/locations/**")
                                .hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/locations/**")
                                .hasAnyAuthority("ADMIN", "ROLE_ADMIN")

                                .requestMatchers(HttpMethod.POST, "/events")
                                .hasAnyAuthority("USER", "ROLE_USER")
                                .requestMatchers(HttpMethod.DELETE, "/events/**")
                                .hasAnyAuthority("ADMIN", "USER", "ROLE_ADMIN", "ROLE_USER")
                                .requestMatchers(HttpMethod.GET, "/events/**")
                                .hasAnyAuthority("ADMIN", "USER", "ROLE_ADMIN", "ROLE_USER")
                                .requestMatchers(HttpMethod.PUT, "/events/**")
                                .hasAnyAuthority("ADMIN", "USER", "ROLE_ADMIN", "ROLE_USER")
                                .requestMatchers(HttpMethod.POST, "/events/search")
                                .hasAnyAuthority("ADMIN", "USER", "ROLE_ADMIN", "ROLE_USER")
                                .requestMatchers(HttpMethod.GET, "/events/my")
                                .hasAnyAuthority("USER", "ROLE_USER")

                                .requestMatchers(HttpMethod.POST, "/events/registrations/**")
                                .hasAnyAuthority("USER", "ROLE_USER")
                                .requestMatchers(HttpMethod.DELETE, "/events/registrations/cancel/**")
                                .hasAnyAuthority("USER", "ROLE_USER")
                                .requestMatchers(HttpMethod.GET, "/events/registrations/my")
                                .hasAnyAuthority( "USER", "ROLE_USER")



                                .anyRequest().authenticated())
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler))
                .addFilterBefore(jwtTokenFilter, AnonymousAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        var authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
