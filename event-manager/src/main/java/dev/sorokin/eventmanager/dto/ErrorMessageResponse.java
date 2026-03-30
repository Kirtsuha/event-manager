package dev.sorokin.eventmanager.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorMessageResponse {
    private String message;
    private String detailedMessage;
    private LocalDateTime dateTime;

    public ErrorMessageResponse(String message, String detailedMessage) {
        this.message = message;
        this.detailedMessage = detailedMessage;
        this.dateTime = LocalDateTime.now();
    }

    public ErrorMessageResponse(String message, String detailedMessage, LocalDateTime dateTime) {
        this.message = message;
        this.detailedMessage = detailedMessage;
        this.dateTime = dateTime;
    }
}
