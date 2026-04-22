package com.clinicapi.config;

import com.clinicapi.entity.Role;
import com.clinicapi.entity.User;
import com.clinicapi.enums.RoleName;
import com.clinicapi.enums.UserStatus;
import com.clinicapi.repository.RoleRepository;
import com.clinicapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class RoleSeederConfig {

    @Bean
    public CommandLineRunner seedRoles(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (roleRepository.findByName(RoleName.ROLE_ADMIN).isEmpty()) {
                roleRepository.save(new Role(RoleName.ROLE_ADMIN));
            }

            if (roleRepository.findByName(RoleName.ROLE_USER).isEmpty()) {
                roleRepository.save(new Role(RoleName.ROLE_USER));
            }

            if (userRepository.findByEmail("admin@clinic.com").isEmpty()) {
                Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                        .orElseThrow();

                User adminUser = new User(
                        "Admin Clinic",
                        "admin@clinic.com",
                        passwordEncoder.encode("admin123"),  // Password: admin123
                        UserStatus.ACTIVE
                );

                adminUser.addRole(adminRole);
                userRepository.save(adminUser);
            }
        };
    }
}