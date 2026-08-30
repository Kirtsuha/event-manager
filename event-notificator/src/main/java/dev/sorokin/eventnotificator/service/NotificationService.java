package dev.sorokin.eventnotificator.service;

import dev.sorokin.eventnotificator.dto.NotificationResponse;
import dev.sorokin.eventnotificator.mapper.NotificationMapper;
import dev.sorokin.eventnotificator.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    @Transactional
    public void markAsRead(Long userId, List<Long> notificationIds) {
        if (notificationIds == null || notificationIds.isEmpty()) {
            return;
        }

        Instant readAt = Instant.now();
        notificationRepository.findByIdInAndUserId(notificationIds, userId)
                .stream()
                .filter(notification -> !notification.isRead())
                .forEach(notification -> {
                    notification.setRead(true);
                    notification.setReadAt(readAt);
                });
    }
}
