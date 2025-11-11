package com.barberApp.scheduleService.controllers;

import com.barberApp.scheduleService.dtos.AuthenticatedUserDTO;
import com.barberApp.scheduleService.dtos.CreateScheduleDTO;
import com.barberApp.scheduleService.dtos.ScheduleDTO;
import com.barberApp.scheduleService.enums.User_Role;
import com.barberApp.scheduleService.services.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService service;

    public ScheduleController(ScheduleService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('BARBER')")
    public ResponseEntity<ScheduleDTO> create(@RequestBody @Valid CreateScheduleDTO dto,
                                              @AuthenticationPrincipal AuthenticatedUserDTO authenticatedUser) {
        return ResponseEntity.ok(service.create(dto));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ScheduleDTO> confirm(@PathVariable UUID id,
                                               @AuthenticationPrincipal AuthenticatedUserDTO authenticatedUser) {
        return ResponseEntity.ok(service.confirm(id, authenticatedUser));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ScheduleDTO> cancel(@PathVariable UUID id,
                                              @AuthenticationPrincipal AuthenticatedUserDTO authenticatedUser) {
        return ResponseEntity.ok(service.cancel(id, authenticatedUser));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> list(@AuthenticationPrincipal AuthenticatedUserDTO authenticatedUser) {
        // Usuarios só podem buscar seus próprios schedules
        // Barbers podem buscar schedules onde eles são o barber
        return ResponseEntity.ok(service.list(authenticatedUser));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> get(@PathVariable UUID id,
                                           @AuthenticationPrincipal AuthenticatedUserDTO authenticatedUser) {
        return ResponseEntity.ok(service.get(id, authenticatedUser));
    }
}

