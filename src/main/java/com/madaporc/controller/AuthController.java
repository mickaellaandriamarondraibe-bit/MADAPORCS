package com.madaporc.controller;

import com.madaporc.DTO.LoginDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("titre", "Connexion - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Connexion - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "AuthController");
        model.addAttribute("methodName", "showLogin");
        model.addAttribute("route", "/login");
        model.addAttribute("loginDTO", new LoginDTO());
        return "placeholder";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO dto, Model model, HttpSession session) {
        model.addAttribute("titre", "Connexion - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Connexion - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "AuthController");
        model.addAttribute("methodName", "login");
        model.addAttribute("route", "/login");
        return "placeholder";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
