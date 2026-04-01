package com.clinicapi.dto.response;

public record AuthResponse(
        Long userId,
        String fullName,
        String email,
        String token
) {}