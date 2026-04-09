package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.domain.Event;
import dev.sorokin.eventmanager.domain.Location;
import dev.sorokin.eventmanager.domain.Registration;
import dev.sorokin.eventmanager.domain.Status;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.RegistrationEntity;
import org.springframework.stereotype.Service;

@Service
public class EventMapper {

    private final LocationMapper locationMapper;

    public EventMapper(LocationMapper locationMapper) {
        this.locationMapper = locationMapper;
    }

    public Event entityToDomain(EventEntity event) {
        return Event.builder()
                .id(event.getId())
                .name(event.getName())
                .durationMinutes(event.getDurationMinutes())
                .startAt(event.getStartAt())
                .maxPlaces(event.getMaxPlaces())
                .status(Status.valueOf(event.getStatus()))
                .occupiedPlaces(event.getOccupiedPlaces())
                .location(locationMapper.entityToDomain(event.getLocation()))
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
                // .location(locationMapper.domainToEntity(event.getLocation())) REMOVED
                .build();
    }
}
