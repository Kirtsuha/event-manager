package dev.sorokin.eventmanager.mapper;

import dev.sorokin.eventmanager.domain.Location;
import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.entity.LocationEntity;
import org.springframework.stereotype.Service;

@Service
public class LocationMapper {
    public Location entityToDomain(LocationEntity entity) {
        return Location.builder()
                .id(entity.getId())
                .name(entity.getName())
                .capacity(entity.getCapacity())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .build();
    }

    public LocationEntity domainToEntity(Location location) {
        return LocationEntity.builder()
                .id(location.getId())
                .name(location.getName())
                .description(location.getDescription())
                .address(location.getAddress())
                .capacity(location.getCapacity())
                .build();
    }

    public Location dtoToDomain(LocationDto dto) {
        return Location.builder()
                .id(dto.getId())
                .name(dto.getName())
                .address(dto.getAddress())
                .capacity(dto.getCapacity())
                .description(dto.getDescription())
                .build();
    }

    public LocationDto domainToDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .capacity(location.getCapacity())
                .description(location.getDescription())
                .build();
    }
}
