package com.clinicapi.service.impl;

import com.clinicapi.dto.request.LoginRequest;
import com.clinicapi.dto.request.RegisterRequest;
import com.clinicapi.dto.response.AuthResponse;
import com.clinicapi.entity.Role;
import com.clinicapi.entity.User;
import com.clinicapi.enums.RoleName;
import com.clinicapi.enums.UserStatus;
import com.clinicapi.exception.BusinessException;
import com.clinicapi.exception.ResourceNotFoundException;
import com.clinicapi.repository.RoleRepository;
import com.clinicapi.repository.UserRepository;
import com.clinicapi.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email is already in use");
        }

        Role defaultRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role ROLE_USER not found"));

        User user = new User(
                request.getFullName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                UserStatus.ACTIVE
        );

        user.addRole(defaultRole);

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                "JWT_TOKEN_WILL_BE_ADDED_LATER"
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new BusinessException("Invalid email or password");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("User is inactive");
        }

        return new AuthResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                "JWT_TOKEN_WILL_BE_ADDED_LATER"
        );
    }
}