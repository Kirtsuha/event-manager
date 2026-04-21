package dev.sorokin.eventmanager.domain;

import dev.sorokin.eventmanager.entity.RegistrationEntity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String login;

    private Long id;

    private String password;

    private Role role;

    private Integer age;
}
