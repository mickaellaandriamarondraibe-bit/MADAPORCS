package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SalaireEmployeController {

    @GetMapping("/salaires")
    public String list(Model model) {
        model.addAttribute("titre", "Salaires employés");
        model.addAttribute("referenceFigma", "Salaires employés");
        model.addAttribute("message", "Salaires employés - list");
        return "placeholder";
    }

}
