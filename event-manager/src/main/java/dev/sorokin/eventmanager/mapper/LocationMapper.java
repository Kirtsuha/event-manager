package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.domain.Location;
import dev.sorokin.eventmanager.entity.LocationEntity;
import org.springframework.stereotype.Service;

@Service
public class LocationMapper {
    public Location entityToDomain(LocationEntity locationEntity) {
        return null;
    }

    public LocationEntity domainToEntity(Location location) {
    }
}
