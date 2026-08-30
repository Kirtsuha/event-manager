package dev.sorokin.eventnotificator.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sorokin.eventnotificator.dto.NotificationResponse;
import dev.sorokin.eventnotificator.entity.NotificationEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    private final ObjectMapper objectMapper;

    public NotificationMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public NotificationResponse toResponse(NotificationEntity entity) {
        var payload = entity.getPayload();
        return new NotificationResponse(
                entity.getId(),
                payload.getEventType(),
                payload.getEventId(),
                entity.getCreatedAt(),
                entity.isRead(),
                buildMessage(payload.getEventType()),
                readPayload(payload.getPayloadJson())
        );
    }

    private JsonNode readPayload(String payloadJson) {
        try {
            return objectMapper.readTree(payloadJson);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Failed to read notification payload", ex);
        }
    }

    private String buildMessage(String eventType) {
        return switch (eventType) {
            case "EVENT_STATUS_CHANGED" -> "Event status was changed";
            case "EVENT_CANCELLED" -> "Event was cancelled";
            default -> "Event was changed";
        };
    }
}
