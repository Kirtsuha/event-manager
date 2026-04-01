package dev.sorokin.eventmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpDto {
    @NotBlank
    @Size(min = 5)
    private String login;

    @NotBlank
    @Size(min = 5)
    private String password;
}
