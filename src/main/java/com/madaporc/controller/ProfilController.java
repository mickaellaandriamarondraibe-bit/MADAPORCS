package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.service.ProfilService;

@Controller
public class ProfilController {
    private final ProfilService service;

    public ProfilController(ProfilService service) {
        this.service = service;
    }

    @GetMapping("/profil")
    public String index(Model model, HttpSession session) {
        return "settings/profil";
    }
}
