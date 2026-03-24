package dev.sorokin.eventmanager.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorMessageResponse {
    private String message;
    private String detailedMessage;
    private LocalDateTime dateTime;

    public ErrorMessageResponse(String message, String detailedMessage) {
        this.message = message;
        this.detailedMessage = detailedMessage;
        this.dateTime = LocalDateTime.now();
    }
}
