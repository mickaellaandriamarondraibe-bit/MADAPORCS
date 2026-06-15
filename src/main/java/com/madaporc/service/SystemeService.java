package com.madaporc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.model.Utilisateur;
import com.madaporc.repository.PermissionRepository;
import com.madaporc.repository.RolePermissionRepository;
import com.madaporc.repository.UtilisateurRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class SystemeService {

    private final UtilisateurRepository utilisateurRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public SystemeService(UtilisateurRepository utilisateurRepository, PermissionRepository permissionRepository, RolePermissionRepository rolePermissionRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public Utilisateur getUtilisateurConnecte(HttpSession session) {
        Long id = (Long) session.getAttribute("userId");
        return id == null ? null : utilisateurRepository.findById(id).orElse(null);
    }

    public boolean verifierUtilisateurConnecte(HttpSession session) {
        return session != null && session.getAttribute("userId") != null;
    }

    public String verifierPermission(Long utilisateurId, String codePermission) {
        if (utilisateurId == null) return "Utilisateur non connecté.";
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);
        if (utilisateur == null) return "Utilisateur introuvable.";
        if (permissionRepository.findByCode(codePermission).isEmpty()) return "Permission introuvable.";
        if (rolePermissionRepository.findByRoleId(utilisateur.getRoleId()).isEmpty()) return "Permission refusée.";
        return null;
    }

    public String validerDonneesFormulaire(Object dto) {
        return dto == null ? "Formulaire vide." : null;
    }

    public String genererCodeUnique(String prefixe) {
        return prefixe + "-" + System.currentTimeMillis();
    }

    public <T> List<T> paginer(List<T> liste, int page, int taille) {
        int debut = Math.max(0, page * taille);
        int fin = Math.min(liste.size(), debut + taille);
        return debut >= liste.size() ? List.of() : liste.subList(debut, fin);
    }

    public String archiverElement(Long id, String tableLogique) {
        return id == null ? "Identifiant invalide." : null;
    }
}
