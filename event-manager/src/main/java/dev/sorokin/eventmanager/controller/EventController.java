package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.EventCreateRequestDto;
import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.dto.EventSearchRequestDto;
import dev.sorokin.eventmanager.dto.EventUpdateRequestDto;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService service;
    private final EventMapper mapper;

    public EventController(EventService service, EventMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<EventDto> create(@Valid @RequestBody EventCreateRequestDto dto, @AuthenticationPrincipal String username) {
        EventDto response = mapper.domainToDto(service.createEvent(mapper.createDtoToDomain(dto), username));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal String username) {
        service.deleteEvent(id, username);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> get(@PathVariable Long id) {
        EventDto response = mapper.domainToDto(service.getEvent(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDto> update(@PathVariable Long id, @Valid @RequestBody EventUpdateRequestDto dto, @AuthenticationPrincipal String username) {
        EventDto response = mapper.domainToDto(service.updateEvent(id, mapper.updateDtoToDomain(dto), username));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/search")
    public ResponseEntity<List<EventDto>> search(@Valid @RequestBody EventSearchRequestDto dto) {
        List<EventDto> response = service.search(dto).stream().map(mapper::domainToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMy(@AuthenticationPrincipal String username) {
        List<EventDto> response = service.getMyEvent(username).stream().map(mapper::domainToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
