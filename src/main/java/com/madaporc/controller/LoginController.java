package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("titre", "Connexion");
        model.addAttribute("referenceFigma", "Connexion");
        model.addAttribute("message", "Connexion - loginForm");
        return "placeholder";
    }


}
