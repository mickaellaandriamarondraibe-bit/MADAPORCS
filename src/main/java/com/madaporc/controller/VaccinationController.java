package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VaccinationController {

    @GetMapping("/vaccinations")
    public String list(Model model) {
        model.addAttribute("titre", "Vaccinations");
        model.addAttribute("referenceFigma", "Vaccinations");
        model.addAttribute("message", "Vaccinations - list");
        return "placeholder";
    }


}
