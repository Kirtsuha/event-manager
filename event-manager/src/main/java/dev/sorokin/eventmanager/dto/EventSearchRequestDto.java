package dev.sorokin.eventmanager.dto;

import dev.sorokin.eventmanager.domain.Status;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventSearchRequestDto {
    private String name;

    private Integer maxPlaces;

    private LocalDateTime dateStartAfter;

    private LocalDateTime dateStartBefore;

    private Integer costMin;

    private Integer costMax;

    private Integer durationMin;

    private Integer durationMax;

    private Integer locationId;

    private Status eventStatus;
}
