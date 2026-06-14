package com.madaporc.controller;

import com.madaporc.DTO.ChangerMotDePasseDTO;
import com.madaporc.DTO.ProfilUtilisateurDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfilController {

    @GetMapping("/profil")
    public String profil(HttpSession session, Model model) {
        model.addAttribute("titre", "Profil Utilisateur - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Profil Utilisateur - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "ProfilController");
        model.addAttribute("methodName", "profil");
        model.addAttribute("route", "/profil");
        model.addAttribute("profilUtilisateurDTO", new ProfilUtilisateurDTO());
        model.addAttribute("changerMotDePasseDTO", new ChangerMotDePasseDTO());
        return "placeholder";
    }

    @PostMapping("/profil/save")
    public String saveProfil(@ModelAttribute ProfilUtilisateurDTO dto, HttpSession session, Model model) {
        model.addAttribute("titre", "Profil Utilisateur - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Profil Utilisateur - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "ProfilController");
        model.addAttribute("methodName", "saveProfil");
        model.addAttribute("route", "/profil/save");
        return "placeholder";
    }

    @PostMapping("/profil/password")
    public String changerMotDePasse(@ModelAttribute ChangerMotDePasseDTO dto, HttpSession session, Model model) {
        model.addAttribute("titre", "Profil Utilisateur - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Profil Utilisateur - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "ProfilController");
        model.addAttribute("methodName", "changerMotDePasse");
        model.addAttribute("route", "/profil/password");
        return "placeholder";
    }

}
