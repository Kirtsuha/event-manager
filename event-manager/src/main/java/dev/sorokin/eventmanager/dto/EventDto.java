package dev.sorokin.eventmanager.dto;

import dev.sorokin.eventmanager.domain.Status;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDto {
    private Long id;

    private String name;

    private Long userId;

    private Integer maxPlaces;

    private Integer occupiedPlaces;

    private LocalDateTime date;

    private Integer cost;

    private Integer duration;

    private Long locationId;

    private Status status;
}
