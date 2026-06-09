package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EmployeController {

    @GetMapping("/employes")
    public String list(Model model) {
        model.addAttribute("titre", "Employés");
        model.addAttribute("referenceFigma", "Employés");
        model.addAttribute("message", "Employés - list");
        return "placeholder";
    }

 

}
