package com.madaporc.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.madaporc.DTO.ChangerMotDePasseDTO;
import com.madaporc.DTO.ProfilUtilisateurDTO;
import com.madaporc.model.Utilisateur;
import com.madaporc.repository.UtilisateurRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ProfilService {

    private final UtilisateurRepository utilisateurRepository;

    public ProfilService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }


    public Utilisateur getUtilisateurConnecte(HttpSession session) {
        Long utilisateurId = (Long) session.getAttribute("userId");

        if (utilisateurId == null) {
            return null;
        }

        return utilisateurRepository.findById(utilisateurId).orElse(null);
    }


    public String modifierProfil(Long utilisateurId, ProfilUtilisateurDTO dto) {
        if (utilisateurId == null) {
            return "Utilisateur non connecté.";
        }

        if (dto == null) {
            return "Formulaire obligatoire.";
        }

        if (dto.getNom() == null || dto.getNom().isBlank()) {
            return "Nom obligatoire.";
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return "Email obligatoire.";
        }

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);

        if (utilisateur == null) {
            return "Utilisateur introuvable.";
        }

        Optional<Utilisateur> emailExistant = utilisateurRepository.findByEmail(dto.getEmail());

        if (emailExistant.isPresent() && !emailExistant.get().getId().equals(utilisateurId)) {
            return "Cet email est déjà utilisé.";
        }

        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());

        utilisateurRepository.save(utilisateur);

        return null;
    }


    public String changerMotDePasse(Long utilisateurId, ChangerMotDePasseDTO dto) {
        if (utilisateurId == null) {
            return "Utilisateur non connecté.";
        }

        if (dto == null) {
            return "Formulaire obligatoire.";
        }

        if (dto.getAncienMotDePasse() == null || dto.getAncienMotDePasse().isBlank()) {
            return "Ancien mot de passe obligatoire.";
        }

        if (dto.getNouveauMotDePasse() == null || dto.getNouveauMotDePasse().isBlank()) {
            return "Nouveau mot de passe obligatoire.";
        }

        if (dto.getConfirmation() == null || dto.getConfirmation().isBlank()) {
            return "Confirmation obligatoire.";
        }

        if (!dto.getNouveauMotDePasse().equals(dto.getConfirmation())) {
            return "La confirmation du mot de passe est incorrecte.";
        }

        if (dto.getNouveauMotDePasse().length() < 6) {
            return "Le nouveau mot de passe doit contenir au moins 6 caractères.";
        }

        if (!verifierAncienMotDePasse(utilisateurId, dto.getAncienMotDePasse())) {
            return "Ancien mot de passe incorrect.";
        }

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);

        if (utilisateur == null) {
            return "Utilisateur introuvable.";
        }

        utilisateur.setMotDePasseHash(hasherMotDePasse(dto.getNouveauMotDePasse()));

        utilisateurRepository.save(utilisateur);

        return null;
    }


    public boolean verifierAncienMotDePasse(Long utilisateurId, String ancienMotDePasse) {
        if (utilisateurId == null || ancienMotDePasse == null || ancienMotDePasse.isBlank()) {
            return false;
        }

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);

        if (utilisateur == null || utilisateur.getMotDePasseHash() == null) {
            return false;
        }

        String hashActuel = utilisateur.getMotDePasseHash();

        return hashActuel.equals(ancienMotDePasse)
                || hashActuel.equals("HASH_" + ancienMotDePasse)
                || hashActuel.equals(hasherMotDePasse(ancienMotDePasse));
    }


    public String hasherMotDePasse(String motDePasse) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(motDePasse.getBytes(StandardCharsets.UTF_8));

            StringBuilder resultat = new StringBuilder();

            for (byte b : hash) {
                resultat.append(String.format("%02x", b));
            }

            return resultat.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hashage du mot de passe.", e);
        }
    }
}