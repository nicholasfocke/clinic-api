package com.clinicapi.dto.response;

import com.clinicapi.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientResponse(
        Long id,
        String fullName,
        String cpf,
        String email,
        String phone,
        LocalDate birthDate,
        Gender gender,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
