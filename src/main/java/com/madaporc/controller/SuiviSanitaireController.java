package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SuiviSanitaireController {

    @GetMapping("/suivis-sanitaires")
    public String list(Model model) {
        model.addAttribute("titre", "Suivi sanitaire");
        model.addAttribute("referenceFigma", "Suivi sanitaire");
        model.addAttribute("message", "Suivi sanitaire - list");
        return "placeholder";
    }


}
