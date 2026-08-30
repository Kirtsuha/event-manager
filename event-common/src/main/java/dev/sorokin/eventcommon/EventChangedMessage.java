package dev.sorokin.eventcommon;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record EventChangedMessage(
    UUID messageId,
    EventType eventType,
    Long eventId,
    Instant occurredAt,
    Long ownerId,
    Long changedById,
    List<Long> subscribers,
    List<EventChange> changes
) {};
