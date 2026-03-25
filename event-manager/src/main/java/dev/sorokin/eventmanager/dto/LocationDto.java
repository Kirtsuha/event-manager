package dev.sorokin.eventmanager.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDto {

    @Null(message = "ID не должен быть прописан")
    private Long id;

    @NotBlank(message = "Name должно быть не пустым")
    @Size(min = 3, max = 255, message = "Длина name должна быть между 3 и 255 символами")
    private String name;

    @NotBlank(message = "Address должно быть не пустым")
    @Size(min = 3, max = 255, message = "Длина address должна быть между 3 и 255 символами")
    private String address;

    @NotNull(message = "Capacity должно быть не пустым")
    @Positive(message = "Capacity должно быть положительным числом")
    private Integer capacity;

    @NotBlank(message = "Description должно быть не пустым")
    @Size(max = 2000, message = "Длина description должна быть короче 2000 символов")
    private String description;

}
