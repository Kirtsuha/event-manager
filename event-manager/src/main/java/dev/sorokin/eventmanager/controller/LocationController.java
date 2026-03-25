package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.domain.Location;
import dev.sorokin.eventmanager.dto.LocationDto;
import dev.sorokin.eventmanager.mapper.LocationMapper;
import dev.sorokin.eventmanager.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {
    private final LocationService service;
    private final LocationMapper mapper;

    @Autowired
    public LocationController(LocationService service, LocationMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<LocationDto> create(@Valid @RequestBody LocationDto dto) {
        Location location = service.createLocation(mapper.dtoToDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.domainToDto(location));
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAll() {
        List<Location> locations = service.getAllLocations();
        List<LocationDto> response = locations.stream()
                .map(mapper::domainToDto)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> get(@PathVariable Long id) {
        Location location = service.getLocation(id);
        return ResponseEntity.status(HttpStatus.OK).body(mapper.domainToDto(location));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteLocation(id);
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> update(@PathVariable Long id, @RequestBody LocationDto dto) {
        Location location = service.updateLocation(id, mapper.dtoToDomain(dto));
        return ResponseEntity.status(HttpStatus.OK).body(mapper.domainToDto(location));
    }
}
