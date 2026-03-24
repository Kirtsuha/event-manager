package dev.sorokin.eventmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@Table(name = "location")
@AllArgsConstructor
@NoArgsConstructor
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", nullable = false, length = 255)
    private String name;

    @Column(name="address", nullable = false, length = 255)
    private String address;

    @Column(name="capacity", nullable = false)
    @Min(value=1, message = "Capacity must be a positive number")
    private Integer capacity;

    @Column(name="description", length = 2000)
    private String description;
}
