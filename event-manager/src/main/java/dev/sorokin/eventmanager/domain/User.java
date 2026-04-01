package dev.sorokin.eventmanager.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String login;

    private Long id;

    private String password;

    private Role role;
}
