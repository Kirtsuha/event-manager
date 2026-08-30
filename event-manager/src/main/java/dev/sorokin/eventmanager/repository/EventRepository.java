package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    List<EventEntity> findByLocation(LocationEntity location);

    List<EventEntity> findByUser(UserEntity user);

    List<EventEntity> findByStatusAndStartAtLessThanEqual(String status, LocalDateTime now);

    @Query(value = "SELECT * FROM events e " +
            "WHERE status = 'STARTED' " +
            "AND e.start_at + (e.duration_minutes * INTERVAL '1 minute') <= :now",
            nativeQuery = true)
    List<EventEntity> findStartedEventsToFinish(@Param("now") LocalDateTime now);
}
