package com.clinicapi.repository;

import com.clinicapi.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByCrm(String crm);

    boolean existsByEmail(String email);

    boolean existsByCrmAndIdNot(String crm, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}
