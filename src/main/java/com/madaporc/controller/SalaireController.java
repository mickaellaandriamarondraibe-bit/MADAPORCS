package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.service.SalaireService;

@Controller
public class SalaireController {
    private final SalaireService service;

    public SalaireController(SalaireService service) {
        this.service = service;
    }

    @GetMapping("/salaires")
    public String index(Model model, HttpSession session) {
        return "salaires/list";
    }
}
