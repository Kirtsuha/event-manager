package dev.sorokin.eventnotificator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessageResponse {
    private String message;
    private String detailedMessage;
    private LocalDateTime dateTime;
}
