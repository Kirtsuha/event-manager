package dev.sorokin.eventmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateRequestDto {

    @NotBlank(message = "Поле name должно быть не пустым")
    @Size(min = 3, max = 255, message = "Длина name должна быть между 3 и 255 символами")
    private String name;

    @NotNull
    @Positive(message = "maxPlaces должно быть положительным числом")
    private Integer maxPlaces;

    @Future(message = "Событие должно быть в будущем")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime date;

    @NotNull(message = "Поле cost должно быть не пустым")
    @PositiveOrZero(message = "Значение cost должно быть положительным числом или нулем")
    private Integer cost;

    @NotNull(message = "Поле duration должно быть не пустым")
    @Positive(message = "Значение cost должно быть положительным числом")
    private Integer duration;

    @NotNull(message = "Поле locationId должно быть не пустым")
    private Long locationId;
}
