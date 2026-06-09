package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class VenteController {

    @GetMapping("/ventes")
    public String list(Model model) {
        model.addAttribute("titre", "Ventes");
        model.addAttribute("referenceFigma", "Ventes");
        model.addAttribute("message", "Ventes - list");
        return "placeholder";
    }

    

}
