package com.clinicapi.dto.response;

import java.time.LocalDateTime;

public record DoctorResponse(
        Long id,
        String fullName,
        String specialty,
        String crm,
        String email,
        String phone,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
