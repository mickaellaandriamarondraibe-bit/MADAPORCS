package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfilController {

    @GetMapping("/profil")
    public String index(Model model) {
        model.addAttribute("titre", "Profil utilisateur");
        model.addAttribute("referenceFigma", "Profil utilisateur");
        model.addAttribute("message", "Profil utilisateur - index");
        return "placeholder";
    }


}
