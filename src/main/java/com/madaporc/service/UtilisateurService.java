package com.madaporc.service;

import com.madaporc.dto.UtilisateurDTO;
import com.madaporc.model.Role;
import com.madaporc.model.Utilisateur;
import com.madaporc.repository.RoleRepository;
import com.madaporc.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        if (dto.getRoleId() == null
                || dto.getMotDePasse() == null || dto.getMotDePasse().isBlank()) {
            return "error";
        }
        if (emailExiste(dto.getEmail())) {
            return "error";
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        utilisateur.setActif(true);

        Role role = roleRepository.findById(dto.getRoleId()).orElse(null);
        if (role == null) {
            return "error";
        }
        utilisateur.setRole(role);

        utilisateurRepository.save(utilisateur);
        return "redirect:/utilisateurs";
    }

    public String modifier(Long id, UtilisateurDTO dto) {
        if (dto.getRoleId() == null) {
            return "error";
        }
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);
        if (utilisateur == null) {
            return "error";
        }

        if (!utilisateur.getEmail().equals(dto.getEmail()) && emailExiste(dto.getEmail())) {
            return "error";
        }

        utilisateur.setNom(dto.getNom());
        utilisateur.setPrenom(dto.getPrenom());
        utilisateur.setEmail(dto.getEmail());

        if (dto.getMotDePasse() != null && !dto.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }

        if (dto.getActif() != null) {
            utilisateur.setActif(dto.getActif());
        }

        Role role = roleRepository.findById(dto.getRoleId()).orElse(null);
        if (role == null) {
            return "error";
        }
        utilisateur.setRole(role);

        utilisateurRepository.save(utilisateur);
        return "redirect:/utilisateurs";
    }

    public String desactiver(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);
        if (utilisateur == null) {
            return "error";
        }
        utilisateur.setActif(false);
        utilisateurRepository.save(utilisateur);
        return "redirect:/utilisateurs";
    }

    public boolean emailExiste(String email) {
        return utilisateurRepository.existsByEmail(email);
    }
}