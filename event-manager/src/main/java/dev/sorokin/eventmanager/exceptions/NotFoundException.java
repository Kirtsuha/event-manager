package dev.sorokin.eventmanager.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName, Long id) {
        super(String.format("%s not found with id: %d", entityName, id));
    }
}
