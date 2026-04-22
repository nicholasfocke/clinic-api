package com.clinicapi.dto.response;

import com.clinicapi.enums.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long id,
        Long doctorId,
        String doctorName,
        Long patientId,
        String patientName,
        LocalDateTime appointmentDateTime,
        Integer durationMinutes,
        AppointmentStatus status,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
