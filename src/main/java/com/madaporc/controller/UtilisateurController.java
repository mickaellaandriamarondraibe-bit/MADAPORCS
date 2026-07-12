package com.madaporc.controller;

import com.madaporc.dto.UtilisateurDTO;
import com.madaporc.model.Role;
import com.madaporc.model.Utilisateur;
import com.madaporc.repository.RoleRepository;
import com.madaporc.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/utilisateurs")
    public String listUtilisateurs(Model model) {
        List<Utilisateur> utilisateurs = utilisateurService.findAll();
        model.addAttribute("utilisateurs", utilisateurs);
        return "utilisateurs/liste";
    }

    @GetMapping("/utilisateurs/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        UtilisateurDTO dto = new UtilisateurDTO();

        if (id != null) {
            Utilisateur utilisateur = utilisateurService.findById(id);
            if (utilisateur == null) {
                // Id inexistant : ne pas transformer silencieusement en creation.
                return "redirect:/utilisateurs";
            }
            dto.setId(utilisateur.getId());
            dto.setNom(utilisateur.getNom());
            dto.setPrenom(utilisateur.getPrenom());
            dto.setEmail(utilisateur.getEmail());
            dto.setRoleId(utilisateur.getRole().getId());
            dto.setActif(utilisateur.getActif());
        }

        List<Role> roles = roleRepository.findAll();
        model.addAttribute("utilisateurDTO", dto);
        model.addAttribute("roles", roles);
        return "utilisateurs/form";
    }

    @PostMapping("/utilisateurs/save")
    public String save(@ModelAttribute UtilisateurDTO dto, Model model) {
        String result;

        if (dto.getId() != null) {
            result = utilisateurService.modifier(dto.getId(), dto);
        } else {
            result = utilisateurService.creer(dto);
        }

        if (result == null || !result.startsWith("redirect:")) {
            List<Role> roles = roleRepository.findAll();
            model.addAttribute("utilisateurDTO", dto);
            model.addAttribute("roles", roles);
            model.addAttribute("error", result);
            return "utilisateurs/form";
        }

        return result;
    }

    @PostMapping("/utilisateurs/desactiver/{id}")
    public String desactiver(@PathVariable Long id) {
        utilisateurService.desactiver(id);
        return "redirect:/utilisateurs";
    }
}