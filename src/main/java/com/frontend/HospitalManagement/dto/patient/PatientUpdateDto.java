package com.frontend.HospitalManagement.dto.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientUpdateDto {

    private String name;
    private String address;
    private String phone;
    private Integer insuranceID;
    private Integer pcpId;
}