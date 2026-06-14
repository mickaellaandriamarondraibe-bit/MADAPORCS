package com.madaporc.controller;

import com.madaporc.DTO.RolePermissionDTO;
import com.madaporc.DTO.UtilisateurDTO;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/utilisateurs/form")
    public String showUtilisateurForm(@RequestParam(required = false) Long id, Model model, HttpSession session) {
        model.addAttribute("titre", "Gestion des Utilisateurs - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Utilisateurs - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "UtilisateurController");
        model.addAttribute("methodName", "showUtilisateurForm");
        model.addAttribute("route", "/utilisateurs/form");
        model.addAttribute("utilisateurDTO", new UtilisateurDTO());
        return "placeholder";
    }

    @PostMapping("/utilisateurs/save")
    public String saveUtilisateur(@ModelAttribute UtilisateurDTO dto, Model model, HttpSession session) {
        model.addAttribute("titre", "Gestion des Utilisateurs - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Utilisateurs - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "UtilisateurController");
        model.addAttribute("methodName", "saveUtilisateur");
        model.addAttribute("route", "/utilisateurs/save");
        return "placeholder";
    }

    @PostMapping("/utilisateurs/desactiver/{id}")
    public String desactiver(@PathVariable Long id, Model model, HttpSession session) {
        model.addAttribute("titre", "Gestion des Utilisateurs - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Utilisateurs - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "UtilisateurController");
        model.addAttribute("methodName", "desactiver");
        model.addAttribute("route", "/utilisateurs/desactiver/" + id);
        return "placeholder";
    }

    @PostMapping("/roles/permissions/save")
    public String saveRolePermissions(@ModelAttribute RolePermissionDTO dto, Model model, HttpSession session) {
        model.addAttribute("titre", "Gestion des Utilisateurs - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Utilisateurs - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "UtilisateurController");
        model.addAttribute("methodName", "saveRolePermissions");
        model.addAttribute("route", "/roles/permissions/save");
        return "placeholder";
    }

}
