package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.domain.Registration;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.RegistrationEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import dev.sorokin.eventmanager.exceptions.NotFoundException;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.mapper.RegistrationMapper;
import dev.sorokin.eventmanager.repository.EventRepository;
import dev.sorokin.eventmanager.repository.RegistrationRepository;
import dev.sorokin.eventmanager.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RegistrationService {
    private final RegistrationRepository repository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RegistrationMapper mapper;

    public RegistrationService(RegistrationRepository repository, UserRepository userRepository, EventRepository eventRepository, RegistrationMapper mapper) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Registration create(Long id, String currentUsername) {
        UserEntity user = userRepository.getByLogin(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + currentUsername + " not found"));

        if (Objects.equals(user.getRole(), "ADMIN")) {
            throw new AccessDeniedException("This method is only available for regular users, not administrators");
        }

        EventEntity event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("event", id));
        if (!Objects.equals(event.getStatus(), "WAIT_START")) {
            throw new IllegalArgumentException("Can't register to an event with status: " + event.getStatus());
        }
        if (Objects.equals(event.getMaxPlaces(), event.getOccupiedPlaces())) {
            throw new IllegalArgumentException("Not enough places for this event");
        }
        if (repository.findByEventAndUser(event, user).isPresent()) {
            throw new IllegalArgumentException("User already has a registration for this event");
        }

        event.setOccupiedPlaces(event.getOccupiedPlaces() + 1);
        var registration = RegistrationEntity.builder()
                .id(null)
                .event(event)
                .user(user)
                .build();
        event.getRegistrations().add(registration);
        user.getRegistrations().add(registration);

        eventRepository.save(event);
        userRepository.save(user);
        return mapper.entityToDomain(repository.save(registration));
    }

    @Transactional
    public void delete(Long id, String currentUsername) {
        UserEntity user = userRepository.getByLogin(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + currentUsername + " not found"));
        EventEntity event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("event", id));

        if (Objects.equals(user.getRole(), "ADMIN")) {
            throw new AccessDeniedException("This method is only available for regular users, not administrators");
        }

        RegistrationEntity registration = repository.findByEventAndUser(event, user)
                .orElseThrow(() -> new IllegalArgumentException("User does not have a registration for this event"));
        event.getRegistrations().remove(registration);
        user.getRegistrations().remove(registration);
        repository.delete(registration);
        userRepository.save(user);
        eventRepository.save(event);
    }

    @Transactional
    public List<Registration> getMy(String currentUsername) {
        UserEntity user = userRepository.getByLogin(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User with login " + currentUsername + " not found"));
        if (Objects.equals(user.getRole(), "ADMIN")) {
            throw new AccessDeniedException("This method is only available for regular users, not administrators");
        }

        return repository.findByUser(user).stream().map(mapper::entityToDomain).toList();
    }

}
