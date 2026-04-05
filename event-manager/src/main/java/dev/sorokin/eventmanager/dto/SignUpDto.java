package dev.sorokin.eventmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpDto {
    @NotBlank(message = "Login cannot be blank")
    @Size(min = 5)
    private String login;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 5)
    private String password;

    @Positive(message = "Age must be a positive number")
    private Integer age;
}
