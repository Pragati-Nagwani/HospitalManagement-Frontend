package com.frontend.HospitalManagement.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private Integer appointmentId;
    private String physicianName;
    private String starto; 
    private String endo;
    private String examinationRoom;
}