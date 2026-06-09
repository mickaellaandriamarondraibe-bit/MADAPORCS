package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String index(Model model) {
        model.addAttribute("titre", "Tableau de bord");
        model.addAttribute("referenceFigma", "Tableau de bord");
        model.addAttribute("message", "Tableau de bord - index");
        return "placeholder";
    }

}
