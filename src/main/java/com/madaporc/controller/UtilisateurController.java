package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UtilisateurController {

    @GetMapping("/utilisateurs")
    public String list(Model model) {
        model.addAttribute("titre", "Utilisateurs et rôles");
        model.addAttribute("referenceFigma", "Utilisateurs et rôles");
        model.addAttribute("message", "Utilisateurs et rôles - list");
        return "placeholder";
    }

}
