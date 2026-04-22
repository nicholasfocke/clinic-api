package com.clinicapi.mapper;

import com.clinicapi.dto.request.PatientRequest;
import com.clinicapi.dto.response.PatientResponse;
import com.clinicapi.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    public Patient toEntity(PatientRequest request) {
        return new Patient(
                request.getFullName(),
                request.getCpf(),
                request.getEmail(),
                request.getPhone(),
                request.getBirthDate(),
                request.getGender(),
                true
        );
    }

    public PatientResponse toResponse(Patient patient) {
        return new PatientResponse(
                patient.getId(),
                patient.getFullName(),
                patient.getCpf(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getBirthDate(),
                patient.getGender(),
                patient.isActive(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }
}
