package com.barberApp.scheduleService.services;

import com.barberApp.scheduleService.dtos.AuthenticatedUserDTO;
import com.barberApp.scheduleService.dtos.CreateScheduleDTO;
import com.barberApp.scheduleService.dtos.ScheduleDTO;
import com.barberApp.scheduleService.enums.ScheduleStatus;
import com.barberApp.scheduleService.enums.User_Role;
import com.barberApp.scheduleService.infra.clients.UserServiceClient;
import com.barberApp.scheduleService.models.Schedule;
import com.barberApp.scheduleService.repositories.ScheduleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    private final ScheduleRepository repository;
    private final UserServiceClient userClient;

    @Value("${user.service.validateBarberRole:false}")
    private boolean validateBarberRole;

    public ScheduleService(ScheduleRepository repository, UserServiceClient userClient) {
        this.repository = repository;
        this.userClient = userClient;
    }

    @Transactional
    public ScheduleDTO create(CreateScheduleDTO dto) {
        if (dto.startTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("startTime must be in the future");
        }

        if (repository.existsByBarberIdAndStartTime(dto.barberId(), dto.startTime())) {
            throw new IllegalStateException("Schedule already exists for this barber at this time");
        }

        // Validate that user and barber exist
        if (userClient.getUser(dto.userId()) == null) {
            throw new IllegalArgumentException("User not found");
        }
        if (userClient.getUser(dto.barberId()) == null) {
            throw new IllegalArgumentException("Barber user not found");
        }
        // Optionally validate role if endpoint is available
        if (validateBarberRole) {
            Boolean barber = userClient.isBarber(dto.barberId());
            if (barber == null || !barber) {
                throw new IllegalArgumentException("Provided barberId is not a barber");
            }
        }

        Schedule schedule = new Schedule();
        schedule.setUserId(dto.userId());
        schedule.setBarberId(dto.barberId());
        schedule.setStartTime(dto.startTime());
        schedule.setStatus(ScheduleStatus.PENDING);

        Schedule saved = repository.save(schedule);
        return toDTO(saved);
    }

    @Transactional
    public ScheduleDTO confirm(UUID id, AuthenticatedUserDTO authenticatedUser) {
        Schedule s = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        // Verificar se o usuário tem permissão (é o usuário do agendamento, o barbeiro ou admin)
        if (!canAccessSchedule(s, authenticatedUser)) {
            throw new AccessDeniedException("You don't have permission to confirm this schedule");
        }

        s.setStatus(ScheduleStatus.CONFIRMED);
        return toDTO(s);
    }

    @Transactional
    public ScheduleDTO cancel(UUID id, AuthenticatedUserDTO authenticatedUser) {
        Schedule s = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        // Verificar se o usuário tem permissão (é o usuário do agendamento, o barbeiro ou admin)
        if (!canAccessSchedule(s, authenticatedUser)) {
            throw new AccessDeniedException("You don't have permission to cancel this schedule");
        }

        s.setStatus(ScheduleStatus.CANCELED);
        return toDTO(s);
    }

    public ScheduleDTO get(UUID id, AuthenticatedUserDTO authenticatedUser) {
        Schedule s = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        // Verificar se o usuário tem permissão para ver este schedule
        if (!canAccessSchedule(s, authenticatedUser)) {
            throw new AccessDeniedException("You don't have permission to view this schedule");
        }

        return toDTO(s);
    }

    public List<ScheduleDTO> list(AuthenticatedUserDTO authenticatedUser) {
        // Admins podem ver todos
        if (User_Role.ADMIN.name().equals(authenticatedUser.role())) {
            return repository.findAll().stream().map(this::toDTO).toList();
        }

        // Barbeiros veem schedules onde são o barbeiro
        if (User_Role.BARBER.name().equals(authenticatedUser.role())) {
            return repository.findByBarberId(authenticatedUser.id()).stream().map(this::toDTO).toList();
        }

        // Usuários comuns veem apenas seus próprios schedules
        return repository.findByUserId(authenticatedUser.id()).stream().map(this::toDTO).toList();
    }

    private boolean canAccessSchedule(Schedule schedule, AuthenticatedUserDTO authenticatedUser) {
        // Admin pode acessar tudo
        if (User_Role.ADMIN.name().equals(authenticatedUser.role())) {
            return true;
        }

        // Usuário é o cliente ou o barbeiro do agendamento
        return schedule.getUserId().equals(authenticatedUser.id())
            || schedule.getBarberId().equals(authenticatedUser.id());
    }

    private ScheduleDTO toDTO(Schedule s) {
        return new ScheduleDTO(s.getId(), s.getUserId(), s.getBarberId(), s.getStartTime(), s.getStatus());
    }
}

