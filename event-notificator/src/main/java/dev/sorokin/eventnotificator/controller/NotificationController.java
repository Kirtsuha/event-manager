package dev.sorokin.eventnotificator.controller;

import dev.sorokin.eventnotificator.dto.MarkNotificationsAsReadRequest;
import dev.sorokin.eventnotificator.dto.NotificationResponse;
import dev.sorokin.eventnotificator.security.AuthenticatedUser;
import dev.sorokin.eventnotificator.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public List<NotificationResponse> getUnreadNotifications(@AuthenticationPrincipal AuthenticatedUser user) {
        return notificationService.getUnreadNotifications(user.userId());
    }

    @PostMapping
    public ResponseEntity<Void> markAsRead(
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody MarkNotificationsAsReadRequest request
    ) {
        notificationService.markAsRead(user.userId(), request.notificationIds());
        return ResponseEntity.noContent().build();
    }
}
