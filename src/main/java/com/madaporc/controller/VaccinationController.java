package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.service.VaccinationService;

@Controller
public class VaccinationController {
    private final VaccinationService service;

    public VaccinationController(VaccinationService service) {
        this.service = service;
    }

    @GetMapping("/vaccinations")
    public String index(Model model, HttpSession session) {
        return "vaccinations/list";
    }
}
