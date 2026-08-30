package dev.sorokin.eventmanager.service;

import dev.sorokin.eventcommon.EventChange;
import dev.sorokin.eventcommon.EventChangedMessage;
import dev.sorokin.eventcommon.EventType;
import dev.sorokin.eventmanager.domain.Event;
import dev.sorokin.eventmanager.sender.EventChangeSender;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class EventNotificationSender {

    private final EventChangeSender sender;
    private final RegistrationService registrationService;

    public EventNotificationSender(EventChangeSender sender, RegistrationService registrationService) {
        this.sender = sender;
        this.registrationService = registrationService;
    }

    public void notifyCancelled(Event oldEvent, Event cancelledEvent, Long userChangerId) {
        List<EventChange> changes = collectChanges(oldEvent, cancelledEvent);
        if (changes.isEmpty()) {
            return;
        }

        var message = new EventChangedMessage(
                UUID.randomUUID(),
                EventType.EVENT_CANCELLED,
                cancelledEvent.getId(),
                Instant.now(),
                cancelledEvent.getUserId(),
                userChangerId,
                registrationService.getUsersIdByEventId(cancelledEvent.getId()),
                changes
        );

        sender.sendEvent(message);
    }

    public void notifyStatusChanged(Event oldEvent, Event newEvent, Long userChangerId) {
        List<EventChange> changes = collectChanges(oldEvent, newEvent);
        if (changes.isEmpty()) {
            return;
        }

        var message = new EventChangedMessage(
                UUID.randomUUID(),
                EventType.EVENT_STATUS_CHANGED,
                newEvent.getId(),
                Instant.now(),
                newEvent.getUserId(),
                userChangerId,
                registrationService.getUsersIdByEventId(newEvent.getId()),
                changes
        );

        sender.sendEvent(message);
    }

    public void notifyUpdated(Event oldEvent, Event newEvent, Long userChangerId) {
        List<EventChange> changes = collectChanges(oldEvent, newEvent);
        if (changes.isEmpty()) {
            return;
        }

        var message = new EventChangedMessage(
                UUID.randomUUID(),
                EventType.EVENT_UPDATED,
                newEvent.getId(),
                Instant.now(),
                newEvent.getUserId(),
                userChangerId,
                registrationService.getUsersIdByEventId(newEvent.getId()),
                changes
        );

        sender.sendEvent(message);
    }

    private List<EventChange> collectChanges(Event oldEvent, Event newEvent) {
        List<EventChange> changes = new ArrayList<>();

        addChange(changes, "name", oldEvent.getName(), newEvent.getName());
        addChange(changes, "startAt", oldEvent.getStartAt(), newEvent.getStartAt());
        addChange(changes, "durationMinutes", oldEvent.getDurationMinutes(), newEvent.getDurationMinutes());
        addChange(changes, "cost", oldEvent.getCost(), newEvent.getCost());
        addChange(changes, "maxPlaces", oldEvent.getMaxPlaces(), newEvent.getMaxPlaces());
        addChange(changes, "locationId", oldEvent.getLocationId(), newEvent.getLocationId());
        addChange(changes, "status", oldEvent.getStatus(), newEvent.getStatus());

        return changes;
    }

    private void addChange(List<EventChange> changes, String field, Object oldValue, Object newValue) {
        if (!Objects.equals(oldValue, newValue)) {
            changes.add(new EventChange(field, oldValue, newValue));
        }
    }
}
