package com.frontend.HospitalManagement.dto.appointment;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCreateDto {
    private Integer appointmentId;
    private Integer patientSsn;   // Passed from the previous page
    private Integer physicianId;
    private Integer nurseId;      // Optional in your Entity
    private String starto;        // From datetime-local input
    private String endo;          // From datetime-local input
    private String examinationRoom;
}
