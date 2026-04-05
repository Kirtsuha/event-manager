package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.JwtTokenResponse;
import dev.sorokin.eventmanager.dto.SignInDto;
import dev.sorokin.eventmanager.dto.SignUpDto;
import dev.sorokin.eventmanager.dto.UserDto;
import dev.sorokin.eventmanager.mapper.UserMapper;
import dev.sorokin.eventmanager.security.jwt.JwtAuthenticationService;
import dev.sorokin.eventmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService service;
    private final UserMapper mapper;
    private final JwtAuthenticationService jwtAuthenticationService;

    public UsersController(UserService service, UserMapper mapper, JwtAuthenticationService jwtAuthenticationService) {
        this.service = service;
        this.mapper = mapper;
        this.jwtAuthenticationService = jwtAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody SignUpDto signUpRequest) {
        var user = service.registerUser(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.domainToDto(user));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(@Valid @RequestBody SignInDto signInRequest) {
        var token = jwtAuthenticationService.authenticateUser(signInRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new JwtTokenResponse(token));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUsers(@PathVariable Long id) {
        var user = service.getUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.domainToDto(user));
    }
}
