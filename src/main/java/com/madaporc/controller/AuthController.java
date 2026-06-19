package com.madaporc.controller;

import com.madaporc.DTO.LoginDTO;
import com.madaporc.model.Utilisateur;
import com.madaporc.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;

    public AuthController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginDTO dto, Model model, HttpSession session) {
        // Vérification simplifiée (à remplacer par vrai service avec hash de mot de passe)
        Utilisateur utilisateur = utilisateurRepository.findByEmail(dto.getEmail()).orElse(null);
        
        if (utilisateur != null && utilisateur.getMotDePasse().equals(dto.getMotDePasse())) {
            if (utilisateur.getStatut() != null && "Actif".equals(utilisateur.getStatut().getNom())) {
                session.setAttribute("utilisateur", utilisateur);
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Votre compte est inactif.");
            }
        } else {
            model.addAttribute("error", "Email ou mot de passe incorrect.");
        }
        
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
