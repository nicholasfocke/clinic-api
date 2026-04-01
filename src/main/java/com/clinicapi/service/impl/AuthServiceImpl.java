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
import com.clinicapi.security.JwtService;
import com.clinicapi.service.AuthService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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

        UserDetails userDetails = buildUserDetails(savedUser);
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail(),
                token
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Invalid email or password"));

        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new BusinessException("Invalid email or password");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException("User is inactive");
        }

        UserDetails userDetails = buildUserDetails(user);
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                token
        );
    }

    private UserDetails buildUserDetails(User user) {
        Set<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities =
                user.getRoles()
                        .stream()
                        .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName().name()))
                        .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}