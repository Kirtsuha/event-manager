package dev.sorokin.eventmanager.exceptions;

import dev.sorokin.eventmanager.dto.ErrorMessageResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotReadable(
            HttpMessageNotReadableException ex) {

        String detailedMessage = ex.getMessage();

        if (detailedMessage.contains("Unexpected character")) {
            detailedMessage = "Invalid JSON format: " + detailedMessage;
        } else if (detailedMessage.contains("Required request body is missing")) {
            detailedMessage = "Request body is required";
        }

        ErrorMessageResponse error = ErrorMessageResponse.builder()
                .message("Malformed JSON request")
                .detailedMessage(detailedMessage)
                .dateTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        String detailedMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorMessageResponse error = ErrorMessageResponse.builder()
                .message("Validation failed")
                .detailedMessage(detailedMessage)
                .dateTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessageResponse> handleConstraintViolation(
            ConstraintViolationException ex) {

        String detailedMessage = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));

        ErrorMessageResponse error = ErrorMessageResponse.builder()
                .message("Validation failed")
                .detailedMessage(detailedMessage)
                .dateTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFound(
            NotFoundException ex) {

        ErrorMessageResponse error = ErrorMessageResponse.builder()
                .message("Entity not found")
                .detailedMessage(ex.getMessage())
                .dateTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleGenericException(
            Exception ex) {

        ErrorMessageResponse error = ErrorMessageResponse.builder()
                .message("Internal server error")
                .detailedMessage(ex.getMessage())
                .dateTime(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
