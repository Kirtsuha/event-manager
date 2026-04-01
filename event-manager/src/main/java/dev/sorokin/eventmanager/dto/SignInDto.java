package dev.sorokin.eventmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignInDto {
    @NotBlank(message = "Login cannot be blank")
    private String login;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
