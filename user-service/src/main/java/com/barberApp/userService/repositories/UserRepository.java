package com.barberApp.userService.repositories;

import com.barberApp.userService.enums.User_Role;
import com.barberApp.userService.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Page<User> findByUserRole(User_Role userRole, Pageable pageable);
}
