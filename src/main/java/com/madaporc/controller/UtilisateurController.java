package com.madaporc.controller;

import com.madaporc.DTO.RolePermissionDTO;
import com.madaporc.DTO.UtilisateurDTO;
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
public class UtilisateurController {

    @GetMapping("/utilisateurs")
    public String listUtilisateurs(Model model, HttpSession session) {
        model.addAttribute("titre", "Gestion des Utilisateurs - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Utilisateurs - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "UtilisateurController");
        model.addAttribute("methodName", "listUtilisateurs");
        model.addAttribute("route", "/utilisateurs");
        return "placeholder";
    }

}
