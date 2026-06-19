package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.service.SuiviSanitaireService;

@Controller
public class SuiviSanitaireController {
    private final SuiviSanitaireService service;

    public SuiviSanitaireController(SuiviSanitaireService service) {
        this.service = service;
    }

    @GetMapping("/sante/suivis")
    public String index(Model model, HttpSession session) {
        return "sante/suivis/list";
    }
}
