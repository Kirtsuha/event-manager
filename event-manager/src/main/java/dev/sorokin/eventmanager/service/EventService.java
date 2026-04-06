package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.domain.Event;
import dev.sorokin.eventmanager.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public Event createEvent(Event event) {
        return null;
    }
}
