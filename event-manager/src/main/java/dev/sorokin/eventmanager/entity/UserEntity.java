package dev.sorokin.eventmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false, unique = true, length = 255)
    private String login;

    @Column(name = "password", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "age", nullable = true)
    @Positive
    private Integer age;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<RegistrationEntity> registrations;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<EventEntity> events;
}
