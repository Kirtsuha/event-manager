package dev.sorokin.eventmanager.controller;

import dev.sorokin.eventmanager.dto.EventDto;
import dev.sorokin.eventmanager.mapper.EventMapper;
import dev.sorokin.eventmanager.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events/registrations/")
public class RegistrationController {

    private final RegistrationService service;
    private final EventMapper eventMapper;

    public RegistrationController(RegistrationService service, EventMapper eventMapper) {
        this.service = service;
        this.eventMapper = eventMapper;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> create(@PathVariable Long id, @AuthenticationPrincipal String username) {
        service.create(id, username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal String username) {
        service.delete(id, username);
        return ResponseEntity.status(204).build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventDto>> getMy(@AuthenticationPrincipal String username) {
        var events = service.getMy(username).stream().map(eventMapper::domainToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(events);
    }

}
