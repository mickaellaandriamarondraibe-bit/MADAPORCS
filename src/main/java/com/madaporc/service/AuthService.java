package com.madaporc.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.madaporc.DTO.LoginDTO;
import com.madaporc.model.Utilisateur;
import com.madaporc.repository.UtilisateurRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;

    public AuthService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public String connecter(LoginDTO dto, HttpSession session) {
        Optional<Utilisateur> optional = findByEmail(dto.getEmail());
        if (optional.isEmpty()) return "Email ou mot de passe incorrect.";
        Utilisateur utilisateur = optional.get();
        if (!utilisateurActif(utilisateur)) return "Compte désactivé.";
        if (!verifierMotDePasse(dto.getMotDePasse(), utilisateur.getMotDePasseHash())) return "Email ou mot de passe incorrect.";
        session.setAttribute("userId", utilisateur.getId());
        session.setAttribute("roleId", utilisateur.getRoleId());
        session.setAttribute("nom", utilisateur.getNom());
        return null;
    }

    public boolean verifierMotDePasse(String brut, String hash) {
        return brut != null && hash != null && (brut.equals(hash) || hash.endsWith(brut));
    }

    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    public boolean utilisateurActif(Utilisateur utilisateur) {
        return utilisateur != null && !Long.valueOf(2L).equals(utilisateur.getStatutUtilisateurId());
    }

    public String redirectionSelonRole(Utilisateur utilisateur) {
        return "redirect:/dashboard";
    }
}
