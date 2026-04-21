package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.entity.EventEntity;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>, JpaSpecificationExecutor<EventEntity> {

    List<EventEntity> findByLocation(LocationEntity location);

    List<EventEntity> findByUser(UserEntity user);

    @Modifying
    @Query("UPDATE EventEntity e " +
            "SET e.status = 'STARTED' " +
            "WHERE e.status = 'WAIT_START' AND e.startAt <= :now")
    void updateWaitingToStarted(@Param("now") LocalDateTime now);

    @Modifying
    @Query(value = "UPDATE events e " +
            "SET status = 'FINISHED' " +
            "WHERE status = 'STARTED' " +
            "AND e.start_at + (e.duration_minutes * INTERVAL '1 minute') <= :now",
            nativeQuery = true)
    void updateStartedToFinished(@Param("now") LocalDateTime now);
}
