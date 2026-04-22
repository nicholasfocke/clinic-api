package com.clinicapi.service.impl;

import com.clinicapi.dto.request.DoctorRequest;
import com.clinicapi.dto.response.DoctorResponse;
import com.clinicapi.dto.response.PageResponse;
import com.clinicapi.entity.Doctor;
import com.clinicapi.exception.BusinessException;
import com.clinicapi.exception.ResourceNotFoundException;
import com.clinicapi.mapper.DoctorMapper;
import com.clinicapi.mapper.PageResponseMapper;
import com.clinicapi.repository.DoctorRepository;
import com.clinicapi.service.DoctorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final PageResponseMapper pageResponseMapper;

    public DoctorServiceImpl (
            DoctorRepository doctorRepository,
            DoctorMapper doctorMapper,
            PageResponseMapper pageResponseMapper
    ){
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.pageResponseMapper = pageResponseMapper;
    }

    @Override
    public DoctorResponse create(DoctorRequest request) {
        if (doctorRepository.existsByCrm(request.getCrm())) {
            throw new BusinessException("CRM is already in use");
        }

        if (doctorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email is already in use");
        }

        Doctor saved = doctorRepository.save(doctorMapper.toEntity(request));
        return doctorMapper.toResponse(saved);
    }

    @Override
    public DoctorResponse findById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return doctorMapper.toResponse(doctor);
    }

    @Override
    public PageResponse<DoctorResponse> findAll(Pageable pageable) {
        Page<DoctorResponse> page = doctorRepository.findAll(pageable).map(doctorMapper::toResponse);
        return pageResponseMapper.toPageResponse(page);
    }

    @Override
    public DoctorResponse update(Long id, DoctorRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        if (doctorRepository.existsByCrmAndIdNot(request.getCrm(), id)) {
            throw new BusinessException("CRM is already in use");
        }

        if (doctorRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new BusinessException("Email is already in use");
        }

        doctor.setFullName(request.getFullName());
        doctor.setSpecialty(request.getSpecialty());
        doctor.setCrm(request.getCrm());
        doctor.setEmail(request.getEmail());
        doctor.setPhone(request.getPhone());

        Doctor updated = doctorRepository.save(doctor);
        return doctorMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        doctor.setActive(false);
        doctorRepository.save(doctor);
    }
}
