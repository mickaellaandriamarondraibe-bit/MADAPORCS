package com.madaporc.service;

import com.madaporc.dto.LoginDTO;
import com.madaporc.model.Utilisateur;
import com.madaporc.repository.UtilisateurRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String connecter(LoginDTO dto, HttpSession session) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(dto.getEmail());

        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            if (utilisateur.getActif() && verifierMotDePasse(dto.getMotDePasse(), utilisateur.getMotDePasse())) {
                session.setAttribute("userId", utilisateur.getId());
                session.setAttribute("roleId", utilisateur.getRole().getId());
                session.setAttribute("nom", utilisateur.getNom());
                return "placeholder";
            }
        }

        return "login";
    }

    public boolean verifierMotDePasse(String motDePasseBrut, String motDePasseHash) {
        return passwordEncoder.matches(motDePasseBrut, motDePasseHash);
    }

    public void deconnecter(HttpSession session) {
        session.invalidate();
    }
}
