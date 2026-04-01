package com.clinicapi.service.impl;

import com.clinicapi.dto.response.PageResponse;
import com.clinicapi.dto.response.UserResponse;
import com.clinicapi.entity.User;
import com.clinicapi.exception.ResourceNotFoundException;
import com.clinicapi.mapper.PageResponseMapper;
import com.clinicapi.mapper.UserMapper;
import com.clinicapi.repository.UserRepository;
import com.clinicapi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PageResponseMapper pageResponseMapper;

    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            PageResponseMapper pageResponseMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.pageResponseMapper = pageResponseMapper;
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return userMapper.toResponse(user);
    }

    @Override
    public PageResponse<UserResponse> findAll(Pageable pageable) {
        Page<UserResponse> page = userRepository.findAll(pageable)
                .map(userMapper::toResponse);

        return pageResponseMapper.toPageResponse(page);
    }
}