package com.madaporc.controller;

import com.madaporc.dto.LoginDTO;
import com.madaporc.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String showLogin(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/connexion")
    public String login(@ModelAttribute LoginDTO dto, Model model, HttpServletRequest request) {
        String result = authService.connecter(dto, request.getSession());
        if ("login".equals(result)) {
            model.addAttribute("error", "Email ou mot de passe incorrect, ou compte désactivé");
        } else {
            // Anti session-fixation : on regenere l'identifiant de session apres authentification.
            request.changeSessionId();
        }
        return result;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        authService.deconnecter(session);
        return "redirect:/";
    }
}