package com.clinicapi.service.impl;

import com.clinicapi.dto.request.PatientRequest;
import com.clinicapi.dto.response.PageResponse;
import com.clinicapi.dto.response.PatientResponse;
import com.clinicapi.entity.Patient;
import com.clinicapi.exception.BusinessException;
import com.clinicapi.exception.ResourceNotFoundException;
import com.clinicapi.mapper.PageResponseMapper;
import com.clinicapi.mapper.PatientMapper;
import com.clinicapi.repository.PatientRepository;
import com.clinicapi.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PageResponseMapper pageResponseMapper;

    public PatientServiceImpl(
            PatientRepository patientRepository,
            PatientMapper patientMapper,
            PageResponseMapper pageResponseMapper
    ) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.pageResponseMapper = pageResponseMapper;
    }

    @Override
    public PatientResponse create(PatientRequest request) {
        if (patientRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("CPF is already in use");
        }

        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email is already in use");
        }

        Patient saved = patientRepository.save(patientMapper.toEntity(request));
        return patientMapper.toResponse(saved);
    }

    @Override
    public PatientResponse findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        return patientMapper.toResponse(patient);
    }

    @Override
    public PageResponse<PatientResponse> findAll(Pageable pageable) {
        Page<PatientResponse> page = patientRepository.findAll(pageable).map(patientMapper::toResponse);
        return pageResponseMapper.toPageResponse(page);
    }

    @Override
    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        if (patientRepository.existsByCpfAndIdNot(request.getCpf(), id)) {
            throw new BusinessException("CPF is already in use");
        }

        if (patientRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new BusinessException("Email is already in use");
        }

        patient.setFullName(request.getFullName());
        patient.setCpf(request.getCpf());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setBirthDate(request.getBirthDate());
        patient.setGender(request.getGender());

        Patient updated = patientRepository.save(patient);
        return patientMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        patient.setActive(false);
        patientRepository.save(patient);
    }
}
