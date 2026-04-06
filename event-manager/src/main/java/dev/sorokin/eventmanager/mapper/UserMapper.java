package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.domain.Role;
import dev.sorokin.eventmanager.domain.User;
import dev.sorokin.eventmanager.dto.UserDto;
import dev.sorokin.eventmanager.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User entityToDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .password(entity.getPasswordHash())
                .role(Role.valueOf(entity.getRole()))
                .age(entity.getAge())
                .registrations(entity.getRegistrations().stream().map())
                .build();
    }

    public UserEntity domainToEntity(User domain) {
        return UserEntity.builder()
                .id(domain.getId())
                .login(domain.getLogin())
                .passwordHash(domain.getPassword())
                .role(String.valueOf(domain.getRole()))
                .age(domain.getAge())
                .build();
    }

    public UserDto domainToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .role(user.getRole().name())
                .age(user.getAge())
                .build();
    }
}

