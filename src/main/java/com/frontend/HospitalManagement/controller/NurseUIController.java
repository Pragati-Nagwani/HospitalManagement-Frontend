package com.frontend.HospitalManagement.controller;

import com.frontend.HospitalManagement.dto.Nurse.NurseDTO;
import com.frontend.HospitalManagement.dto.Nurse.NursePageResponse;
import com.frontend.HospitalManagement.service.NurseApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NurseUIController {

    @Autowired
    private NurseApiService nurseApiService;

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

    @GetMapping("/nurses/add")
    public String showAddForm(Model model) {

        model.addAttribute("nurse", new NurseDTO());

        return "nurse/add-nurse";
    }

    @PostMapping("/nurses/save")
    public String saveNurse(@ModelAttribute NurseDTO nurse) {

        nurseApiService.addNurse(nurse);

        return "redirect:/nurse/nurses";
    }

    @GetMapping("/nurses/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {


        NurseDTO nurse = new NurseDTO();
        nurse.setEmployeeId(id);

        model.addAttribute("nurse", nurse);

        return "nurse/edit-nurse";
    }

    @PostMapping("/nurses/update/{id}")
    public String updateNurse(@PathVariable Integer id,
                              @ModelAttribute NurseDTO nurse) {

        nurseApiService.updateNurse(id, nurse);

        return "redirect:/nurse/nurses";
    }

    @GetMapping("/nurses/view/{id}")
    public String viewNurse(@PathVariable Integer id, Model model) {

        // Later we will call:
        // appointment API + oncall API

        model.addAttribute("nurseId", id);

        return "nurse/nurse-details";
    }

    @PostMapping("/nurses/add")
    public String addNurse(@ModelAttribute NurseDTO nurse) {

        nurseApiService.addNurse(nurse);

        return "redirect:/nurses";
    }
}
