package dev.sorokin.eventnotificator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventcommon.EventChangedMessage;
import dev.sorokin.eventnotificator.entity.NotificationEntity;
import dev.sorokin.eventnotificator.entity.NotificationEventPayloadEntity;
import dev.sorokin.eventnotificator.repository.NotificationEventPayloadRepository;
import dev.sorokin.eventnotificator.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class EventChangeConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventChangeConsumer.class);

    private final NotificationEventPayloadRepository payloadRepository;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    public EventChangeConsumer(
            NotificationEventPayloadRepository payloadRepository,
            NotificationRepository notificationRepository,
            ObjectMapper objectMapper
    ) {
        this.payloadRepository = payloadRepository;
        this.notificationRepository = notificationRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${notifications.kafka.topic:event-changes}")
    @Transactional
    public void consume(EventChangedMessage message) {
        if (message == null
                || message.messageId() == null
                || message.eventType() == null
                || message.eventId() == null
                || message.occurredAt() == null) {
            log.warn("Skipping invalid event change message {}", message);
            return;
        }
        if (payloadRepository.existsByMessageId(message.messageId())) {
            log.info("Skipping already processed event change message {}", message.messageId());
            return;
        }

        NotificationEventPayloadEntity payload = payloadRepository.save(NotificationEventPayloadEntity.builder()
                .messageId(message.messageId())
                .eventType(message.eventType().name())
                .eventId(message.eventId())
                .occurredAt(message.occurredAt())
                .ownerId(message.ownerId())
                .changedById(message.changedById())
                .payloadJson(toPayloadJson(message))
                .build());

        Instant createdAt = Instant.now();
        if (message.subscribers() == null) {
            return;
        }

        message.subscribers().stream()
                .distinct()
                .map(userId -> NotificationEntity.builder()
                        .userId(userId)
                        .read(false)
                        .createdAt(createdAt)
                        .payload(payload)
                        .build())
                .forEach(notificationRepository::save);
    }

    private String toPayloadJson(EventChangedMessage message) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("messageId", message.messageId());
        payload.put("eventType", message.eventType());
        payload.put("occurredAt", message.occurredAt());
        payload.put("changedById", message.changedById());
        payload.put("ownerId", message.ownerId());
        payload.put("changes", message.changes());
        payload.put("eventName", extractEventName(message));

        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to serialize notification payload", ex);
        }
    }

    private Object extractEventName(EventChangedMessage message) {
        if (message.changes() == null) {
            return null;
        }

        return message.changes().stream()
                .filter(change -> "name".equals(change.field()))
                .map(change -> change.newValue() == null ? change.oldValue() : change.newValue())
                .findFirst()
                .orElse(null);
    }
}
