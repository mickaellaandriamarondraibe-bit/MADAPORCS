package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.service.RapportService;

@Controller
public class RapportController {
    private final RapportService service;

    public RapportController(RapportService service) {
        this.service = service;
    }

    @GetMapping("/rapports")
    public String index(Model model, HttpSession session) {
        return "rapports/index";
    }
}
