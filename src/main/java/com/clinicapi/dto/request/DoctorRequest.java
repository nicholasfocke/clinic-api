package com.clinicapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DoctorRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 150, message = "Full name must have at most 150 characters")
    private String fullName;

    @NotBlank(message = "Speciality is required")
    @Size(max = 100, message = "Speciality must have at most 100 characters")
    private String speciality;

    @NotBlank(message = "CRM is required")
    @Size(max = 30, message = "CRM must have at most 30 characters")
    private String crm;

    @NotBlank(message = "Email is required")
    @Size(max = 150, message = "Email must have at most 150 characters")
    private String email;

        @NotBlank(message = "Phone is required")
    @Size(max = 20, message = "Phone must have at most 20 characters")
    private String phone;

    public DoctorRequest(){
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
