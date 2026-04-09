package dev.sorokin.eventmanager.repository;

import dev.sorokin.eventmanager.dto.EventSearchRequestDto;
import dev.sorokin.eventmanager.entity.EventEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventSpecification {
    public static Specification<EventEntity> build(EventSearchRequestDto dto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dto.getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        dto.getName().toLowerCase()));
            }

            if (dto.getMaxPlaces() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("maxPlaces"), dto.getMaxPlaces()));
            }

            if (dto.getDateStartAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startAt"), dto.getDateStartAfter()));
            }

            if (dto.getDateStartBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startAt"), dto.getDateStartBefore()));
            }

            if (dto.getCostMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("cost"), dto.getCostMin()));
            }

            if (dto.getCostMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("cost"), dto.getCostMax()));
            }

            if (dto.getDurationMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("durationMinutes"), dto.getDurationMin()));
            }

            if (dto.getDurationMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("durationMinutes"), dto.getDurationMax()));
            }

            if (dto.getLocationId() != null) {
                predicates.add(cb.equal(root.get("location").get("id"), dto.getLocationId()));
            }

            if (dto.getEventStatus() != null) {
                predicates.add(cb.equal(root.get("status"), dto.getEventStatus().name()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
