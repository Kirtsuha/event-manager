package dev.sorokin.eventmanager.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    private Long id;

    private String name;

    private LocalDateTime startAt;

    private Integer durationMinutes;

    private Integer maxPlaces;

    private Integer occupiedPlaces;

    private Status status;

    private Location location;

    private User user;
}
