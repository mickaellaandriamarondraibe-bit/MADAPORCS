package com.madaporc.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.madaporc.service.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String index(Model model) {
        model.addAttribute("dashboard", dashboardService.getDashboard());
        model.addAttribute("misesBasProches", dashboardService.listerMisesBasProches());
        model.addAttribute("dateJour", LocalDate.now());
        return "dashboard/index";
    }
}
