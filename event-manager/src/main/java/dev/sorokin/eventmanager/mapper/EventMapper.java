package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.domain.Event;
import dev.sorokin.eventmanager.domain.Registration;
import dev.sorokin.eventmanager.domain.Status;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.RegistrationEntity;
import org.springframework.stereotype.Service;

@Service
public class EventMapper {

    public Event entityToDomain(EventEntity event) {
        return Event.builder()
                .id(event.getId())
                .name(event.getName())
                .durationMinutes(event.getDurationMinutes())
                .startAt(event.getStartAt())
                .maxPlaces(event.getMaxPlaces())
                .status(Status.valueOf(event.getStatus()))
                .occupiedPlaces(event.getOccupiedPlaces())
                .registrations(event.getRegistrations().stream()
                        .map(this::entityToDomainNoForeignClasses).toList())
                .build();

    }

    public EventEntity domainToEntity(Event event) {
        return EventEntity.builder()
                .id(event.getId())
                .name(event.getName())
                .durationMinutes(event.getDurationMinutes())
                .startAt(event.getStartAt())
                .maxPlaces(event.getMaxPlaces())
                .status(event.getStatus().name())
                .occupiedPlaces(event.getOccupiedPlaces())
                .registrations(event.getRegistrations().stream()
                        .map(this::domainToEntityNoForeignClasses).toList())
                .build();
    }

    private RegistrationEntity domainToEntityNoForeignClasses(Registration domain) {
        return RegistrationEntity.builder()
                .id(domain.getId())
                .user(null)
                .event(null)
                .build();
    }

    private Registration entityToDomainNoForeignClasses(RegistrationEntity entity) {
        return Registration.builder()
                .id(entity.getId())
                .user(null)
                .event(null)
                .build();
    }
}
