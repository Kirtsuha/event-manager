package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.domain.Event;
import dev.sorokin.eventmanager.domain.Status;
import dev.sorokin.eventmanager.dto.EventCreateRequestDto;
import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.dto.EventUpdateRequestDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class EventMapper {

    private final LocationMapper locationMapper;

    public EventMapper(LocationMapper locationMapper) {
        this.locationMapper = locationMapper;
    }

    public Event createDtoToDomain(EventCreateRequestDto dto) {
        return Event.builder()
                .id(null)
                .name(dto.getName())
                .startAt(dto.getDate())
                .durationMinutes(dto.getDuration())
                .maxPlaces(dto.getMaxPlaces())
                .cost(dto.getCost())
                .occupiedPlaces(0)
                .status(Status.WAIT_START)
                .locationId(dto.getLocationId())
                .userId(null)
                .build();
    }

    public Event entityToDomain(EventEntity event) {
        return Event.builder()
                .id(event.getId())
                .name(event.getName())
                .durationMinutes(event.getDurationMinutes())
                .startAt(event.getStartAt())
                .maxPlaces(event.getMaxPlaces())
                .cost(event.getCost())
                .status(Status.valueOf(event.getStatus()))
                .occupiedPlaces(event.getOccupiedPlaces())
                .locationId(event.getLocation().getId())
                .userId(event.getUser().getId())
                .build();

    }

    public EventEntity domainToEntity(Event event) {
        return EventEntity.builder()
                .id(event.getId())
                .name(event.getName())
                .durationMinutes(event.getDurationMinutes())
                .startAt(event.getStartAt())
                .maxPlaces(event.getMaxPlaces())
                .cost(event.getCost())
                .status(event.getStatus().name())
                .occupiedPlaces(event.getOccupiedPlaces())
                // .location(locationMapper.domainToEntity(event.getLocation())) REMOVED
                .build();
    }

    public EventDto domainToDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .duration(event.getDurationMinutes())
                .date(event.getStartAt())
                .maxPlaces(event.getMaxPlaces())
                .cost(event.getCost())
                .status(event.getStatus())
                .occupiedPlaces(event.getOccupiedPlaces())
                .locationId(event.getLocationId())
                .userId(event.getUserId())
                .build();
    }

    public Event updateDtoToDomain(EventUpdateRequestDto dto) {
        return Event.builder()
                .id(null)
                .name(dto.getName())
                .startAt(dto.getDate())
                .durationMinutes(dto.getDuration())
                .maxPlaces(dto.getMaxPlaces())
                .cost(dto.getCost())
                .occupiedPlaces(0)
                .status(Status.WAIT_START)
                .locationId(dto.getLocationId())
                .userId(null)
                .build();
    }
}
