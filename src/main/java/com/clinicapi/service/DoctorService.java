package com.clinicapi.service;

import com.clinicapi.dto.request.DoctorRequest;
import com.clinicapi.dto.response.DoctorResponse;
import com.clinicapi.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface DoctorService {

    DoctorResponse create(DoctorRequest request);

    DoctorResponse findById(Long id);

    PageResponse <DoctorResponse> findAll(Pageable pageable);

    DoctorResponse update(Long id, DoctorRequest request);

    void delete(Long id);
}
