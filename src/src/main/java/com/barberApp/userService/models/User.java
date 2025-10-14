package com.barberApp.userService.models;

import com.barberApp.userService.dtos.RegisterDTO;
import com.barberApp.userService.enums.User_Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@EqualsAndHashCode(of = "id")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    private String email;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String phone;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private User_Role userRole = User_Role.USER;

    @Column(nullable = false)
    private boolean emailConfirmed = false;

    public User(RegisterDTO registerDTO) {
        this.name = registerDTO.name();
        this.email = registerDTO.email();
        this.phone = registerDTO.phone();
        this.password = registerDTO.password();
    }

    public User() {}

    @Override
    public String toString() {
        return "Name: '" + this.name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.userRole == User_Role.ADMIN) {
            return Stream.of(User_Role.values())
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                    .collect(Collectors.toList());
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_" + this.userRole.name()));
        }
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
