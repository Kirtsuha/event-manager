package dev.sorokin.eventcommon;

public record EventChange(
        String field,
        Object oldValue,
        Object newValue
) {
}