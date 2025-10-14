package com.barberApp.userService.infra.adminInitializer;

import com.barberApp.userService.enums.User_Role;
import com.barberApp.userService.models.User;
import com.barberApp.userService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener
    public void initializeAdminUser(ContextRefreshedEvent event) {
        String adminEmail = "admin@example.com";

        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setName("Admin");
            admin.setPhone("17996065627");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin_password"));
            admin.setUserRole(User_Role.ADMIN);
            admin.setEmailConfirmed(true);

            userRepository.save(admin);
            System.out.println("Admin user created successfully!");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
