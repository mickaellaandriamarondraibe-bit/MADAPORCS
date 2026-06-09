package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DistributionAlimentController {

    @GetMapping("/distributions-aliment")
    public String list(Model model) {
        model.addAttribute("titre", "Distribution des aliments");
        model.addAttribute("referenceFigma", "Distribution des aliments");
        model.addAttribute("message", "Distribution des aliments - list");
        return "placeholder";
    }

  
}
