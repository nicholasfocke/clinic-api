package com.clinicapi.service.impl;

import com.clinicapi.dto.request.AppointmentRequest;
import com.clinicapi.dto.response.AppointmentResponse;
import com.clinicapi.dto.response.PageResponse;
import com.clinicapi.entity.Appointment;
import com.clinicapi.entity.Doctor;
import com.clinicapi.entity.Patient;
import com.clinicapi.enums.AppointmentStatus;
import com.clinicapi.exception.BusinessException;
import com.clinicapi.exception.ResourceNotFoundException;
import com.clinicapi.mapper.AppointmentMapper;
import com.clinicapi.mapper.PageResponseMapper;
import com.clinicapi.repository.AppointmentRepository;
import com.clinicapi.repository.DoctorRepository;
import com.clinicapi.repository.PatientRepository;
import com.clinicapi.service.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentMapper appointmentMapper;
    private final PageResponseMapper pageResponseMapper;

    public AppointmentServiceImpl(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            AppointmentMapper appointmentMapper,
            PageResponseMapper pageResponseMapper
    ) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentMapper = appointmentMapper;
        this.pageResponseMapper = pageResponseMapper;
    }

    @Override
    public AppointmentResponse create(AppointmentRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        validateDoctorScheduleConflict(
                request.getDoctorId(),
                request.getAppointmentDateTime(),
                request.getDurationMinutes(),
                null
        );

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(request.getAppointmentDateTime());
        appointment.setDurationMinutes(request.getDurationMinutes());
        appointment.setNotes(request.getNotes());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(saved);
    }

    @Override
    public AppointmentResponse findById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        return appointmentMapper.toResponse(appointment);
    }

    @Override
    public PageResponse<AppointmentResponse> findAll(Pageable pageable) {
        Page<AppointmentResponse> page = appointmentRepository.findAll(pageable)
                .map(appointmentMapper::toResponse);
        return pageResponseMapper.toPageResponse(page);
    }

    @Override
    public AppointmentResponse update(Long id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        validateDoctorScheduleConflict(
                request.getDoctorId(),
                request.getAppointmentDateTime(),
                request.getDurationMinutes(),
                id
        );

        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(request.getAppointmentDateTime());
        appointment.setDurationMinutes(request.getDurationMinutes());
        appointment.setNotes(request.getNotes());

        Appointment updated = appointmentRepository.save(appointment);
        return appointmentMapper.toResponse(updated);
    }

    @Override
    public void confirm(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);
    }

    @Override
    public void cancel(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    private void validateDoctorScheduleConflict(
            Long doctorId,
            LocalDateTime newStart,
            Integer durationMinutes,
            Long currentAppointmentId
    ) {
        LocalDateTime newEnd = newStart.plusMinutes(durationMinutes);

        List<Appointment> existingAppointments = appointmentRepository.findByDoctorIdAndStatusNot(
                doctorId,
                AppointmentStatus.CANCELLED
        );

        for (Appointment existing : existingAppointments) {
            if (currentAppointmentId != null && existing.getId().equals(currentAppointmentId)) {
                continue;
            }

            LocalDateTime existingStart = existing.getAppointmentDateTime();
            LocalDateTime existingEnd = existingStart.plusMinutes(existing.getDurationMinutes());

            boolean overlap = newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd);
            if (overlap) {
                throw new BusinessException("Doctor has a scheduling conflict");
            }
        }
    }
}
