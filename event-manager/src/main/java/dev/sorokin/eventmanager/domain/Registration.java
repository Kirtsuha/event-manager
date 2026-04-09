package dev.sorokin.eventmanager.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Registration {
    private Long id;

    private Long eventId;

    private Long userId;
}
