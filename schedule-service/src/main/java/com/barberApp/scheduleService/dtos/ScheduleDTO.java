package com.barberApp.scheduleService.dtos;

import com.barberApp.scheduleService.enums.ScheduleStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleDTO(
        UUID id,
        UUID userId,
        UUID barberId,
        LocalDateTime startTime,
        ScheduleStatus status
) { }

