package com.clinicapi.service;

import com.clinicapi.dto.request.PatientRequest;
import com.clinicapi.dto.response.PageResponse;
import com.clinicapi.dto.response.PatientResponse;
import org.springframework.data.domain.Pageable;

public interface PatientService {
    PatientResponse create(PatientRequest request);

    PatientResponse findById(Long id);

    PageResponse<PatientResponse> findAll(Pageable pageable);

    PatientResponse update(Long id, PatientRequest request);

    void delete(Long id);
}
