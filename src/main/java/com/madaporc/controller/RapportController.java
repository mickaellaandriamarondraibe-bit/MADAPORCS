package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RapportController {

    @GetMapping("/rapports")
    public String index(Model model) {
        model.addAttribute("titre", "Rapports");
        model.addAttribute("referenceFigma", "Rapports");
        model.addAttribute("message", "Rapports - index");
        return "placeholder";
    }

}
