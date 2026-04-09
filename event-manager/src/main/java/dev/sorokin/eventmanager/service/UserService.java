package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.domain.Role;
import dev.sorokin.eventmanager.domain.User;
import dev.sorokin.eventmanager.dto.SignUpDto;
import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.exceptions.NotFoundException;
import dev.sorokin.eventmanager.mapper.UserMapper;
import dev.sorokin.eventmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    public User registerUser(SignUpDto signUpRequest) {
        if (repository.existsByLogin(signUpRequest.getLogin())) {
            throw new IllegalArgumentException("Username already taken");
        }
        var user = UserEntity.builder()
                .id(null)
                .login(signUpRequest.getLogin())
                .passwordHash(encoder.encode(signUpRequest.getPassword()))
                .role(Role.USER.name())
                .age(signUpRequest.getAge())
                .build();
        return mapper.entityToDomain(repository.save(user));
    }

    public User findByLogin(String login) {
        return mapper.entityToDomain(repository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"))
        );
    }

    public User getUser(Long id) {
        return mapper.entityToDomain(repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User", id)));
    }
}
