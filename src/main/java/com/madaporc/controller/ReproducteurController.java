package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ReproducteurController {

    @GetMapping("/reproducteurs")
    public String list(Model model) {
        model.addAttribute("titre", "Reproducteurs");
        model.addAttribute("referenceFigma", "Reproducteurs");
        model.addAttribute("message", "Reproducteurs - list");
        return "placeholder";
    }

 

}
