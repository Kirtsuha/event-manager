package dev.sorokin.eventmanager.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDto {

    @Null
    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    @NotBlank
    @Size(min = 3, max = 255)
    private String address;

    @NotNull
    @Positive
    private Integer capacity;

    @NotBlank
    @Size(max = 2000)
    private String description;

}
