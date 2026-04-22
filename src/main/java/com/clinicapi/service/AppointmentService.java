package com.clinicapi.service;

import com.clinicapi.dto.request.AppointmentRequest;
import com.clinicapi.dto.response.AppointmentResponse;
import com.clinicapi.dto.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {

    AppointmentResponse create(AppointmentRequest request);

    AppointmentResponse findById(Long id);

    PageResponse<AppointmentResponse> findAll(Pageable pageable);

    AppointmentResponse update(Long id, AppointmentRequest request);

    void confirm(Long id);

    void cancel(Long id);
}
