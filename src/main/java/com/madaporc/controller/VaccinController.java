package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VaccinController {

    @GetMapping("/vaccins")
    public String list(Model model) {
        model.addAttribute("titre", "Vaccins");
        model.addAttribute("referenceFigma", "Vaccins");
        model.addAttribute("message", "Vaccins - list");
        return "placeholder";
    }

}
