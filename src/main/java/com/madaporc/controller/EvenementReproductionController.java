package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EvenementReproductionController {

    @GetMapping("/evenements-reproduction")
    public String list(Model model) {
        model.addAttribute("titre", "Événements de reproduction");
        model.addAttribute("referenceFigma", "Événements de reproduction");
        model.addAttribute("message", "Événements de reproduction - list");
        return "placeholder";
    }

 

}
