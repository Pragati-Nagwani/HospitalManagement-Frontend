package com.frontend.HospitalManagement.dto.Nurse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NurseDTO {

    private Integer employeeId;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Position is required")
    private String position;

    private boolean registered;

    private String availability;
    @NotNull(message = "SSN is required")
    private Integer ssn;
}