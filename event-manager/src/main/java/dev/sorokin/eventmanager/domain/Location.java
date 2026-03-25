package dev.sorokin.eventmanager.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    private Long id;

    private String name;

    private String address;

    private Integer capacity;

    private String description;
}
