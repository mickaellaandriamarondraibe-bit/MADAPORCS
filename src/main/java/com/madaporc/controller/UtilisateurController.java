package com.madaporc.controller;

import com.madaporc.DTO.RolePermissionDTO;
import com.madaporc.DTO.UtilisateurDTO;
import com.madaporc.model.Role;
import com.madaporc.model.StatutUtilisateur;
import com.madaporc.model.Utilisateur;
import com.madaporc.repository.RoleRepository;
import com.madaporc.repository.StatutUtilisateurRepository;
import com.madaporc.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class UtilisateurController {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final StatutUtilisateurRepository statutUtilisateurRepository;

    public UtilisateurController(UtilisateurRepository utilisateurRepository, 
                                  RoleRepository roleRepository, 
                                  StatutUtilisateurRepository statutUtilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.statutUtilisateurRepository = statutUtilisateurRepository;
    }

    @GetMapping("/utilisateurs")
    public String listUtilisateurs(Model model, HttpSession session) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("utilisateur");
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        model.addAttribute("utilisateurs", utilisateurs);
        model.addAttribute("showForm", false);
        return "settings/utilisateurs";
    }

    @GetMapping("/utilisateurs/form")
    public String showUtilisateurForm(@RequestParam(required = false) Long id, Model model, HttpSession session) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("utilisateur");
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        List<Role> roles = roleRepository.findAll();
        List<StatutUtilisateur> statuts = statutUtilisateurRepository.findAll();
        
        model.addAttribute("utilisateurs", utilisateurs);
        model.addAttribute("roles", roles);
        model.addAttribute("statuts", statuts);
        model.addAttribute("showForm", true);
        model.addAttribute("utilisateurDTO", new UtilisateurDTO());
        
        if (id != null) {
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(id);
            utilisateurOpt.ifPresent(u -> model.addAttribute("utilisateurEdit", u));
        }
        
        return "settings/utilisateurs";
    }

    @PostMapping("/utilisateurs/save")
    public String saveUtilisateur(@ModelAttribute UtilisateurDTO dto, Model model, HttpSession session) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("utilisateur");
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        Utilisateur utilisateur;
        if (dto.getId() != null) {
            utilisateur = utilisateurRepository.findById(dto.getId()).orElse(new Utilisateur());
        } else {
            utilisateur = new Utilisateur();
        }
        
        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        
        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(dto.getMotDePasse());
        }
        
        if (dto.getRoleId() != null) {
            roleRepository.findById(dto.getRoleId()).ifPresent(utilisateur::setRole);
        }
        
        if (dto.getStatutUtilisateurId() != null) {
            statutUtilisateurRepository.findById(dto.getStatutUtilisateurId()).ifPresent(utilisateur::setStatut);
        } else if (dto.getId() == null) {
            // Par défaut, actif pour les nouveaux utilisateurs
            statutUtilisateurRepository.findByNom("Actif").ifPresent(utilisateur::setStatut);
        }
        
        utilisateurRepository.save(utilisateur);
        return "redirect:/utilisateurs";
    }

    @PostMapping("/utilisateurs/desactiver/{id}")
    public String desactiver(@PathVariable Long id, Model model, HttpSession session) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("utilisateur");
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        utilisateurRepository.findById(id).ifPresent(utilisateur -> {
            statutUtilisateurRepository.findByNom("Inactif").ifPresent(utilisateur::setStatut);
            utilisateurRepository.save(utilisateur);
        });
        
        return "redirect:/utilisateurs";
    }

    @PostMapping("/roles/permissions/save")
    public String saveRolePermissions(@ModelAttribute RolePermissionDTO dto, Model model, HttpSession session) {
        Utilisateur currentUser = (Utilisateur) session.getAttribute("utilisateur");
        if (currentUser == null) {
            return "redirect:/login";
        }
        // Implémentation à ajouter si nécessaire
        return "redirect:/utilisateurs";
    }

}
