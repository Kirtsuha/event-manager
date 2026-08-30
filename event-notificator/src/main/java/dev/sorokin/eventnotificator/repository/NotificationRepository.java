package dev.sorokin.eventnotificator.repository;

import dev.sorokin.eventnotificator.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByUserIdAndReadFalseOrderByCreatedAtDesc(Long userId);

    List<NotificationEntity> findByIdInAndUserId(List<Long> ids, Long userId);

    void deleteByCreatedAtBefore(Instant cutoff);
}
