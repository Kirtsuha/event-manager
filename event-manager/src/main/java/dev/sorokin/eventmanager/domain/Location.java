package dev.sorokin.eventmanager.domain;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
public class Location {
    private Long id;

    private String name;

    private String address;

    private Integer capacity;

   private String description;
}
