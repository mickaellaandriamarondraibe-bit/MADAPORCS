package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.service.DistributionAlimentService;

@Controller
public class DistributionAlimentController {
    private final DistributionAlimentService service;

    public DistributionAlimentController(DistributionAlimentService service) {
        this.service = service;
    }

    @GetMapping("/distributions")
    public String index(Model model, HttpSession session) {
        return "ressources/distributions";
    }
}
