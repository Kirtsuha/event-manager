package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.domain.*;
import dev.sorokin.eventmanager.dto.EventSearchRequestDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.exceptions.NotFoundException;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.mapper.UserMapper;
import dev.sorokin.eventmanager.repository.EventRepository;
import dev.sorokin.eventmanager.repository.EventSpecification;
import dev.sorokin.eventmanager.repository.LocationRepository;
import dev.sorokin.eventmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository repository;
    private final EventMapper mapper;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public EventService(EventRepository repository, EventMapper mapper, LocationRepository locationRepository, UserRepository userRepository, UserMapper userMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    protected Optional<Event> isLocationOccupied(LocalDateTime startTime, LocalDateTime endTime, LocationEntity location) {
        List<Event> eventEntities = repository.findByLocation(location)
                .stream().map(mapper::entityToDomain).toList();
        return eventEntities.stream().filter(
                event ->
                        (event.getStartAt().isAfter(startTime) && event.getStartAt().isBefore(endTime)) ||
                        (event.getStartAt().plusMinutes(event.getDurationMinutes()).isAfter(startTime) &&
                                event.getStartAt().plusMinutes(event.getDurationMinutes()).isBefore(endTime)) ||
                        (event.getStartAt().isBefore(startTime) && event.getStartAt().plusMinutes(event.getDurationMinutes()).isAfter(endTime))
        ).findFirst();
    }

    @Transactional
    public Event getEvent(Long id) {
        return mapper.entityToDomain(repository.findById(id).orElseThrow(() -> new NotFoundException("event", id)));
    }

    @Transactional
    public Event createEvent(Event event, String currentUsername) {
        event.setStatus(Status.WAIT_START);

        UserEntity user = userRepository.getByLogin(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + currentUsername + " not found"));

        LocationEntity locationEntity = locationRepository.findById(event.getLocationId()).orElseThrow(
                () -> new NotFoundException("location", event.getLocationId())
        );
        if (locationEntity.getCapacity() < event.getMaxPlaces()) {
            throw new IllegalArgumentException("Location capacity exceeded");
        }

        event.setLocationId(locationEntity.getId());
        Optional<Event> occupiedEvent = isLocationOccupied(event.getStartAt(), event.getStartAt().plusMinutes(event.getDurationMinutes()), locationEntity);

        if (occupiedEvent.isPresent()) {
            throw new IllegalArgumentException("Location with id " + event.getLocationId() + " is already occupied in this time by event " + occupiedEvent.get().getId());
        }

        EventEntity entity = mapper.domainToEntity(event);
        entity.setLocation(locationEntity);
        entity.setOccupiedPlaces(0);
        entity.setUser(user);

        return mapper.entityToDomain(repository.save(entity));
    }

    @Transactional
    public void deleteEvent(Long id, String currentUsername) {
        User user = userMapper.entityToDomain(userRepository.getByLogin(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + currentUsername + " not found"))
        );

        EventEntity event = repository.findById(id).orElseThrow(() -> new NotFoundException("event", id));

        if (!(user.getRole() == Role.ADMIN || Objects.equals(event.getUser().getId(), user.getId()))) {
            throw new AccessDeniedException("Only event creator can edit this event");
        }

        if (!Objects.equals(event.getStatus(), "WAIT_START")) {
            throw new IllegalArgumentException("Can't cancel event with status " + event.getStatus());
        }
        event.setStatus("CANCELLED");
        repository.save(event);
    }

    @Transactional
    public Event updateEvent(Long id, Event newEvent, String currentUsername) {
        User user = userMapper.entityToDomain(userRepository.getByLogin(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + currentUsername + " not found"))
        );

        EventEntity entity = repository.findById(id).orElseThrow(() -> new NotFoundException("event", id));

        System.out.println("Current user id: " + user.getId());
        System.out.println("NewEvent user id: " + newEvent.getUserId());
        System.out.println("Is ADMIN: " + (user.getRole() == Role.ADMIN));
        System.out.println("User ids equal: " + Objects.equals(newEvent.getUserId(), user.getId()));

        LocationEntity newLocation = locationRepository.getLocationEntitiesById(newEvent.getLocationId());
        if (newLocation == null) {
            throw new NotFoundException("location", newEvent.getLocationId());
        }
        if (!(user.getRole() == Role.ADMIN || Objects.equals(entity.getUser().getId(), user.getId()))) {
            throw new AccessDeniedException("Only event creator can edit this event");
        }

        Event oldEvent = mapper.entityToDomain(entity);

        if (oldEvent.getStatus() == Status.CANCELLED || oldEvent.getStatus() == Status.FINISHED) {
            throw new IllegalArgumentException("Can't change finished or removed event, make new instead");
        }
        if (oldEvent.getStatus() == Status.STARTED) {
            throw new IllegalArgumentException("Can't change ongoing event");
        }

        if (!oldEvent.getStartAt().minusHours(48).isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Too late to update event");
        }
        if (!newEvent.getStartAt().plusHours(48).isAfter(LocalDateTime.now()) ||
        newEvent.getStartAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can't put time that early");
        }
        if (newEvent.getMaxPlaces() < oldEvent.getOccupiedPlaces()) {
            throw new IllegalArgumentException("Can't set max places less than there are registrations");
        }
        if (newEvent.getMaxPlaces() > newLocation.getCapacity()) {
            throw new IllegalArgumentException("Can't set max places more than max location capacity");
        }

        Optional<Event> occupiedEvent = isLocationOccupied(newEvent.getStartAt(), newEvent.getStartAt().plusMinutes(newEvent.getDurationMinutes()), newLocation);
        if (occupiedEvent.isPresent()) {
            throw new IllegalArgumentException("Location with id " + newEvent.getLocationId() + " is already occupied in this time by event " + occupiedEvent.get().getId());
        }

        entity.setStartAt(newEvent.getStartAt());
        entity.setDurationMinutes(newEvent.getDurationMinutes());
        entity.setMaxPlaces(newEvent.getMaxPlaces());
        entity.setLocation(locationRepository.findById(
                newEvent.getLocationId()).get()
        );
        entity.setName(newEvent.getName());
        return mapper.entityToDomain(repository.save(entity));
    }

    @Transactional
    public List<Event> search(EventSearchRequestDto dto) {
        Specification<EventEntity> spec = EventSpecification.build(dto);
        return repository.findAll(spec).stream().map(mapper::entityToDomain).toList();
    }

    @Transactional
    public List<Event> getMyEvent(String currentUsername) {
        UserEntity userEntity = userRepository.getByLogin(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + currentUsername + " not found")
        );
        return repository.findByUser(userEntity).stream().map(mapper::entityToDomain).toList();
    }
}
