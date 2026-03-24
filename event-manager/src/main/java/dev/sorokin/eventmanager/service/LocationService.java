package dev.sorokin.eventmanager.service;

import dev.sorokin.eventmanager.domain.Location;
import dev.sorokin.eventmanager.entity.LocationEntity;
import dev.sorokin.eventmanager.exceptions.NotFoundException;
import dev.sorokin.eventmanager.mapper.LocationMapper;
import dev.sorokin.eventmanager.repository.LocationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private LocationRepository repository;
    private LocationMapper mapper;

    @Transactional
    public Location getLocation(Long id) {
        LocationEntity entity = repository.getLocationEntitiesById(id);
        if (entity == null) {
            throw new NotFoundException("Location", id);
        }
        return mapper.entityToDomain(entity);
    }

    @Transactional
    public List<Location> getAllLocations() {
        List<LocationEntity> entities = repository.findAll();
        return entities.stream()
                .map(mapper::entityToDomain)
                .toList();
    }

    @Transactional
    public Location createLocation(Location location) {
        LocationEntity entity = mapper.domainToEntity(location);
        return mapper.entityToDomain(repository.save(entity));
    }

    @Transactional
    public Location updateLocation(Long id, Location newLocation) {
        LocationEntity entity = repository.getLocationEntitiesById(id);
        if (entity == null) {
            throw new NotFoundException("Location", id);
        }
        entity.setAddress(newLocation.getAddress());
        entity.setName(newLocation.getAddress());
        entity.setCapacity(newLocation.getCapacity());
        entity.setDescription(newLocation.getDescription());
        return mapper.entityToDomain(repository.save(entity));
    }

    @Transactional
    public void deleteLocation(Long id) {
        repository.deleteById(id);
    }
}
