package com.frontend.HospitalManagement.controller;

import com.frontend.HospitalManagement.dto.Nurse.NurseDTO;
import com.frontend.HospitalManagement.dto.Nurse.NursePageResponse;
import com.frontend.HospitalManagement.service.NurseApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class NurseUIController {

    @Autowired
    private NurseApiService nurseApiService;

    // =========================
    // LIST PAGE
    // =========================
    @GetMapping("/nurses")
    public String getNurses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            Model model
    ) {

        NursePageResponse response = nurseApiService.getNurses(page, size, keyword, null);

        model.addAttribute("nurses", response.getNurses());
        model.addAttribute("totalPages", response.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "nurse/nurses";
    }

    // =========================
    // ADD NURSE
    // =========================
    @GetMapping("/nurses/add")
    public String showAddForm(Model model) {

        model.addAttribute("nurse", new NurseDTO());

        return "nurse/add-nurse";
    }

    @PostMapping("/nurses/add")
    public String addNurse(@ModelAttribute NurseDTO nurse) {

        nurseApiService.addNurse(nurse);

        return "redirect:/nurses";
    }

    // =========================
    // EDIT NURSE
    // =========================
    @GetMapping("/nurses/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {

        NurseDTO nurse = nurseApiService.getNurseById(id);

        model.addAttribute("nurse", nurse);

        return "nurse/edit-nurse";
    }

    @PostMapping("/nurses/update")
    public String updateNurse(@ModelAttribute NurseDTO nurse) {

        nurseApiService.updateNurse(nurse.getEmployeeId(), nurse);

        return "redirect:/nurses";
    }

    // =========================
    // VIEW PAGE
    // =========================
    @GetMapping("/nurses/view/{id}")
    public String viewNurse(@PathVariable Integer id, Model model) {

        NurseDTO nurse = nurseApiService.getNurseById(id);

        Map<String, Object> appointment = nurseApiService.getAppointmentByNurse(id);
        Map<String, Object> oncall = nurseApiService.getOnCallByNurse(id);

        model.addAttribute("nurse", nurse);
        model.addAttribute("appointment", appointment);
        model.addAttribute("oncall", oncall);

        return "nurse/nurse-details";
    }
}