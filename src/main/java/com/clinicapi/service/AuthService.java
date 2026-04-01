package com.clinicapi.service;

import com.clinicapi.dto.request.LoginRequest;
import com.clinicapi.dto.request.RegisterRequest;
import com.clinicapi.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}