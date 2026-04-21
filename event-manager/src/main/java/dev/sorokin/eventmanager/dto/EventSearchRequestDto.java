package dev.sorokin.eventmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.sorokin.eventmanager.domain.Status;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventSearchRequestDto {
    private String name;

    @PositiveOrZero(message = "Поле maxPlaces должно быть положительным или равным нулю")
    private Integer maxPlaces;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime dateStartAfter;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime dateStartBefore;

    @PositiveOrZero(message = "Поле costMin должно быть положительным или равным нулю")
    private Integer costMin;

    @PositiveOrZero(message = "Поле costMax должно быть положительным или равным нулю")
    private Integer costMax;

    @PositiveOrZero(message = "Поле durationMin должно быть положительным или равным нулю")
    private Integer durationMin;

    @PositiveOrZero(message = "Поле durationMax должно быть положительным или равным нулю")
    private Integer durationMax;

    private Integer locationId;

    private Status eventStatus;
}
