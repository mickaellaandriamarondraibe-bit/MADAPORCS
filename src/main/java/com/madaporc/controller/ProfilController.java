package com.madaporc.controller;

import com.madaporc.DTO.ChangerMotDePasseDTO;
import com.madaporc.DTO.ProfilUtilisateurDTO;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfilController {

    @GetMapping("/profil")
    public String profil(HttpSession session, Model model) {
        model.addAttribute("titre", "Profil Utilisateur - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Profil Utilisateur - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "ProfilController");
        model.addAttribute("methodName", "profil");
        model.addAttribute("route", "/profil");
        return "placeholder";
    }


}
