package dev.sorokin.eventnotificator.security;

public record AuthenticatedUser(
        Long userId,
        String login,
        String role
) {
}
