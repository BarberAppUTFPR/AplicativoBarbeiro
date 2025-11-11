package com.barberApp.userService.services;

import com.barberApp.userService.dtos.GetUsersDTO;
import com.barberApp.userService.dtos.UpdateDTO;
import com.barberApp.userService.dtos.UpdateUserRoleDTO;
import com.barberApp.userService.enums.User_Role;
import com.barberApp.userService.models.User;
import com.barberApp.userService.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void update(UpdateDTO dto, UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found."));

        if (dto.name() != null) user.setName(dto.name());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.phone() != null) user.setPhone(dto.phone());

        userRepository.save(user);
    }

    @Transactional
    public void delete(UUID id) {
        try {
            userRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User with id " + id + " not found.");
        }
    }

    @Transactional(readOnly = true)
    public Page<GetUsersDTO> findAll(Pageable pageable) {
        return userRepository.findByUserRole(User_Role.USER, pageable)
                .map(GetUsersDTO::new);
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(@NotBlank String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found."));
    }

    @Transactional(readOnly = true)
    public GetUsersDTO findUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found."));
        return new GetUsersDTO(user);
    }

    @Transactional
    public void updateUserRole(UUID id, UpdateUserRoleDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found."));

        user.setUserRole(dto.role());
        userRepository.save(user);
    }
}
