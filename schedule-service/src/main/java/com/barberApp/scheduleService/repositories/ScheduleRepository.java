package com.barberApp.scheduleService.repositories;

import com.barberApp.scheduleService.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    List<Schedule> findByUserId(UUID userId);
    List<Schedule> findByBarberId(UUID barberId);
    boolean existsByBarberIdAndStartTime(UUID barberId, LocalDateTime startTime);
}

