package com.frontend.HospitalManagement.dto.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDto {
    private Integer ssn; 
    private String name;
    private String address;
    private String phone;
    private String pcpName;  // comes from projection
}