package com.madaporc.controller;

import com.madaporc.DTO.ChangerMotDePasseDTO;
import com.madaporc.DTO.ProfilUtilisateurDTO;
import com.madaporc.model.Utilisateur;
import com.madaporc.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfilController {

    private final UtilisateurRepository utilisateurRepository;

    public ProfilController(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    @GetMapping("/profil")
    public String profil(HttpSession session, Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("profilUtilisateurDTO", new ProfilUtilisateurDTO());
        model.addAttribute("changerMotDePasseDTO", new ChangerMotDePasseDTO());
        return "settings/profil";
    }

    @PostMapping("/profil/save")
    public String saveProfil(@ModelAttribute ProfilUtilisateurDTO dto, HttpSession session, Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/login";
        }
        
        // Mettre à jour le profil
        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur = utilisateurRepository.save(utilisateur);
        
        session.setAttribute("utilisateur", utilisateur);
        model.addAttribute("successProfil", "Profil mis à jour avec succès !");
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("profilUtilisateurDTO", dto);
        model.addAttribute("changerMotDePasseDTO", new ChangerMotDePasseDTO());
        return "settings/profil";
    }

    @PostMapping("/profil/password")
    public String changerMotDePasse(@ModelAttribute ChangerMotDePasseDTO dto, HttpSession session, Model model) {
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        if (utilisateur == null) {
            return "redirect:/login";
        }
        
        // Vérifier l'ancien mot de passe et que la confirmation correspond
        if (utilisateur.getMotDePasse().equals(dto.getAncienMotDePasse()) 
                && dto.getNouveauMotDePasse().equals(dto.getConfirmation())) {
            utilisateur.setMotDePasse(dto.getNouveauMotDePasse());
            utilisateur = utilisateurRepository.save(utilisateur);
            session.setAttribute("utilisateur", utilisateur);
            model.addAttribute("successPassword", "Mot de passe changé avec succès !");
        }
        
        model.addAttribute("utilisateur", utilisateur);
        model.addAttribute("profilUtilisateurDTO", new ProfilUtilisateurDTO());
        model.addAttribute("changerMotDePasseDTO", dto);
        return "settings/profil";
    }

}
