package com.clinicapi.service;

import com.clinicapi.dto.response.PageResponse;
import com.clinicapi.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponse findById(Long id);

    PageResponse<UserResponse> findAll(Pageable pageable);
}