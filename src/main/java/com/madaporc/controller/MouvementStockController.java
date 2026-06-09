package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MouvementStockController {

    @GetMapping("/mouvements-stock")
    public String list(Model model) {
        model.addAttribute("titre", "Mouvements de stock alimentaire");
        model.addAttribute("referenceFigma", "Mouvements de stock alimentaire");
        model.addAttribute("message", "Mouvements de stock alimentaire - list");
        return "placeholder";
    }

}
