package dev.sorokin.eventmanager.domain;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Registration {
    private Long id;

    private Event event;

    private User user;
}
