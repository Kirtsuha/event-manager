package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.domain.Event;
import dev.sorokin.eventmanager.domain.Status;
import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventStatusSchedulerService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final EventNotificationSender notificationSender;

    public EventStatusSchedulerService(
            EventRepository eventRepository,
            EventMapper eventMapper,
            EventNotificationSender notificationSender
    ) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.notificationSender = notificationSender;
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void updateEventStatuses() {
        try {
            LocalDateTime now = LocalDateTime.now();

            eventRepository.findByStatusAndStartAtLessThanEqual(Status.WAIT_START.name(), now)
                    .forEach(event -> changeStatus(event, Status.STARTED));

            eventRepository.findStartedEventsToFinish(now)
                    .forEach(event -> changeStatus(event, Status.FINISHED));
        } catch (Exception e) {
            System.out.println("Failed to update event statuses: " + e.getMessage());
        }

    }

    private void changeStatus(EventEntity event, Status newStatus) {
        Event oldEvent = eventMapper.entityToDomain(event);

        event.setStatus(newStatus.name());
        EventEntity savedEvent = eventRepository.save(event);

        Event updatedEvent = eventMapper.entityToDomain(savedEvent);
        notificationSender.notifyStatusChanged(oldEvent, updatedEvent, null);
    }
}
