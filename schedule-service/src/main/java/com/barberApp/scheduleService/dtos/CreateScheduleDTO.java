package com.barberApp.scheduleService.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateScheduleDTO(
        @NotNull UUID userId,
        @NotNull UUID barberId,
        @NotNull @Future LocalDateTime startTime
) { }

