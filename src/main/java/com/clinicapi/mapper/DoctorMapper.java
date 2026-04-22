package com.clinicapi.mapper;

import com.clinicapi.dto.request.DoctorRequest;
import com.clinicapi.dto.response.DoctorResponse;
import com.clinicapi.entity.Doctor;
import org.springframework.stereotype.Component;

import javax.print.Doc;

@Component
public class DoctorMapper {
    public Doctor toEntity(DoctorRequest request){
        return new Doctor(
                request.getFullName(),
                request.getSpeciality(),
                request.getCrm(),
                request.getEmail(),
                request.getPhone(),
                true
        );
    }

    public DoctorResponse toResponse(Doctor doctor){
        return new DoctorResponse(
                doctor.getId(),
                doctor.getFullName(),
                doctor.getSpeciality(),
                doctor.getCrm(),
                doctor.getEmail(),
                doctor.getPhone(),
                doctor.isActive(),
                doctor.getCreatedAt(),
                doctor.getUpdatedAt()
        );
    }
}
