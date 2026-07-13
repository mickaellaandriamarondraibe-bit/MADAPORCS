package com.madaporc.service;

import com.madaporc.dto.UtilisateurDTO;
import com.madaporc.model.Role;
import com.madaporc.model.Utilisateur;
import com.madaporc.repository.RoleRepository;
import com.madaporc.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Utilisateur> findAll() {
        return utilisateurRepository.findAllWithRole();
    }

    public Utilisateur findById(Long id) {
        return utilisateurRepository.findById(id).orElse(null);
    }

    public String creer(UtilisateurDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return "L'email est obligatoire.";
        }
        if (dto.getMotDePasse() == null || dto.getMotDePasse().isBlank()) {
            return "Le mot de passe est obligatoire.";
        }
        if (dto.getRoleId() == null) {
            return "Le rôle est obligatoire.";
        }
        if (emailExiste(dto.getEmail())) {
            return "Cet email est déjà utilisé.";
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        utilisateur.setActif(dto.getActif() != null ? dto.getActif() : true);

        Role role = roleRepository.findById(dto.getRoleId()).orElse(null);
        if (role == null) {
            return "Rôle introuvable.";
        }
        utilisateur.setRole(role);

        utilisateurRepository.save(utilisateur);
        return "redirect:/utilisateurs";
    }

    @Transactional
    public String modifier(Long id, UtilisateurDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return "L'email est obligatoire.";
        }
        if (dto.getRoleId() == null) {
            return "Le rôle est obligatoire.";
        }
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);
        if (utilisateur == null) {
            return "Utilisateur introuvable.";
        }

        if (!utilisateur.getEmail().equals(dto.getEmail()) && emailExiste(dto.getEmail())) {
            return "Cet email est déjà utilisé.";
        }

        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());

        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }

        if (dto.getActif() != null) {
            // On empeche de desactiver le dernier administrateur actif.
            if (!dto.getActif() && estDernierAdminActif(utilisateur)) {
                return "Impossible de désactiver le dernier administrateur actif.";
            }
            utilisateur.setActif(dto.getActif());
        }

        Role role = roleRepository.findById(dto.getRoleId()).orElse(null);
        if (role == null) {
            return "Rôle introuvable.";
        }
        utilisateur.setRole(role);

        utilisateurRepository.save(utilisateur);
        return "redirect:/utilisateurs";
    }

    @Transactional
    public String desactiver(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);
        if (utilisateur == null) {
            return "Utilisateur introuvable.";
        }
        if (estDernierAdminActif(utilisateur)) {
            return "Impossible de désactiver le dernier administrateur actif.";
        }
        utilisateur.setActif(false);
        utilisateurRepository.save(utilisateur);
        return "redirect:/utilisateurs";
    }

    // Vrai si l'utilisateur est un ADMIN actif et qu'il ne reste aucun autre ADMIN actif.
    private boolean estDernierAdminActif(Utilisateur u) {
        if (u.getRole() == null || !"ADMIN".equalsIgnoreCase(u.getRole().getNom())
                || u.getActif() == null || !u.getActif()) {
            return false;
        }
        long adminsActifs = utilisateurRepository.findAllWithRole().stream()
                .filter(x -> x.getActif() != null && x.getActif())
                .filter(x -> x.getRole() != null && "ADMIN".equalsIgnoreCase(x.getRole().getNom()))
                .count();
        return adminsActifs <= 1;
    }

    public boolean emailExiste(String email) {
        return utilisateurRepository.existsByEmail(email);
    }
}