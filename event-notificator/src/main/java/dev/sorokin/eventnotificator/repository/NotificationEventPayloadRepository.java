package dev.sorokin.eventnotificator.repository;

import dev.sorokin.eventnotificator.entity.NotificationEventPayloadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationEventPayloadRepository extends JpaRepository<NotificationEventPayloadEntity, Long> {

    boolean existsByMessageId(UUID messageId);

    @Modifying
    @Query(value = """
            delete from notification_event_payloads payload
            where not exists (
                select 1 from notifications notification
                where notification.payload_id = payload.id
            )
            """, nativeQuery = true)
    void deleteOrphanPayloads();
}
