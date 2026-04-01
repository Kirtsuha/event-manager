package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.domain.Location;
import dev.sorokin.eventmanager.domain.Role;
import dev.sorokin.eventmanager.domain.User;
import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.dto.UserDto;
import dev.sorokin.eventmanager.entity.LocationEntity;
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
                .build();
    }

    public UserEntity domainToEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .login(user.getLogin())
                .passwordHash(user.getPassword())
                .role(user.getRole().name())
                .build();
    }

    public User dtoToDomain(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .login(dto.getLogin())
                .password(null)
                .role(Role.valueOf(dto.getRole()))
                .build();
    }

    public UserDto domainToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .role(user.getRole().name())
                .build();
    }
}

