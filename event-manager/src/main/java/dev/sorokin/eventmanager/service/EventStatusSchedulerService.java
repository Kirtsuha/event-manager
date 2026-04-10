package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventStatusSchedulerService {

    private final EventRepository eventRepository;

    public EventStatusSchedulerService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void updateEventStatuses() {
        try {
            LocalDateTime now = LocalDateTime.now();

            eventRepository.updateWaitingToStarted(now);

            eventRepository.updateStartedToFinished(now);
        } catch (Exception e) {
            System.out.println("Failed to update event statuses: " + e.getMessage());
        }

    }
}
