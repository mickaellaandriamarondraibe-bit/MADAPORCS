package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MouvementLotController {

    @GetMapping("/mouvements-lots")
    public String list(Model model) {
        model.addAttribute("titre", "Mouvements des lots");
        model.addAttribute("referenceFigma", "Mouvements des lots");
        model.addAttribute("message", "Mouvements des lots - list");
        return "placeholder";
    }

  
}
