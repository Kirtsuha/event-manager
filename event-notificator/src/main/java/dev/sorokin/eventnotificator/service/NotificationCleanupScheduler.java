package dev.sorokin.eventnotificator.service;

import dev.sorokin.eventnotificator.repository.NotificationEventPayloadRepository;
import dev.sorokin.eventnotificator.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class NotificationCleanupScheduler {

    private final NotificationRepository notificationRepository;
    private final NotificationEventPayloadRepository payloadRepository;
    private final long olderThanDays;

    public NotificationCleanupScheduler(
            NotificationRepository notificationRepository,
            NotificationEventPayloadRepository payloadRepository,
            @Value("${notifications.cleanup.older-than-days:7}") long olderThanDays
    ) {
        this.notificationRepository = notificationRepository;
        this.payloadRepository = payloadRepository;
        this.olderThanDays = olderThanDays;
    }

    @Scheduled(cron = "${notifications.cleanup.cron:0 0 * * * *}")
    @Transactional
    public void cleanupOldNotifications() {
        notificationRepository.deleteByCreatedAtBefore(Instant.now().minus(olderThanDays, ChronoUnit.DAYS));
        payloadRepository.deleteOrphanPayloads();
    }
}
