package dev.sorokin.eventmanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;

    private String login;

    private String role;
}
