package com.madaporc.service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madaporc.DTO.RolePermissionDTO;
import com.madaporc.model.Permission;
import com.madaporc.model.Role;
import com.madaporc.model.RolePermission;
import com.madaporc.model.Utilisateur;
import com.madaporc.repository.PermissionRepository;
import com.madaporc.repository.RolePermissionRepository;
import com.madaporc.repository.RoleRepository;
import com.madaporc.repository.UtilisateurRepository;

@Service
public class AdminUtilisateurService {

    private static final Long STATUT_ACTIF_ID = 1L;
    private static final Long STATUT_INACTIF_ID = 2L;

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public AdminUtilisateurService(
            UtilisateurRepository utilisateurRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository
    ) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }


    public String verifierPermissionAdmin(Long utilisateurId) {
        if (utilisateurId == null) {
            return "Utilisateur non connecté.";
        }

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);

        if (utilisateur == null) {
            return "Utilisateur introuvable.";
        }

        if (utilisateur.getRoleId() == null) {
            return "Aucun rôle associé à cet utilisateur.";
        }

        Role role = roleRepository.findById(utilisateur.getRoleId()).orElse(null);

        if (role == null) {
            return "Rôle introuvable.";
        }

        if (role.getLibelle() == null || !role.getLibelle().equalsIgnoreCase("Administrateur")) {
            return "Accès réservé à l'administrateur.";
        }

        return null;
    }


    public String changerRole(Long utilisateurId, Long roleId) {
        if (utilisateurId == null || roleId == null) {
            return "Données invalides.";
        }

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);

        if (utilisateur == null) {
            return "Utilisateur introuvable.";
        }

        if (!roleRepository.existsById(roleId)) {
            return "Rôle introuvable.";
        }

        utilisateur.setRoleId(roleId);
        utilisateur.setUpdatedAt(LocalDateTime.now());

        utilisateurRepository.save(utilisateur);

        return null;
    }


    public String desactiverUtilisateur(Long utilisateurId) {
        if (utilisateurId == null) {
            return "Utilisateur invalide.";
        }

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);

        if (utilisateur == null) {
            return "Utilisateur introuvable.";
        }

        utilisateur.setStatutUtilisateurId(STATUT_INACTIF_ID);
        utilisateur.setUpdatedAt(LocalDateTime.now());

        utilisateurRepository.save(utilisateur);

        return null;
    }


    public String activerUtilisateur(Long utilisateurId) {
        if (utilisateurId == null) {
            return "Utilisateur invalide.";
        }

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);

        if (utilisateur == null) {
            return "Utilisateur introuvable.";
        }

        utilisateur.setStatutUtilisateurId(STATUT_ACTIF_ID);
        utilisateur.setUpdatedAt(LocalDateTime.now());

        utilisateurRepository.save(utilisateur);

        return null;
    }


    @Transactional
    public String affecterPermissions(RolePermissionDTO dto) {
        if (dto == null) {
            return "Permissions obligatoires.";
        }

        if (dto.getRoleId() == null) {
            return "Rôle obligatoire.";
        }

        if (!roleRepository.existsById(dto.getRoleId())) {
            return "Rôle introuvable.";
        }

        rolePermissionRepository.deleteByRoleId(dto.getRoleId());

        if (dto.getPermissionIds() != null) {
            for (Long permissionId : dto.getPermissionIds()) {
                if (permissionId != null && permissionRepository.existsById(permissionId)) {
                    RolePermission rolePermission = new RolePermission();

                    rolePermission.setRoleId(dto.getRoleId());
                    rolePermission.setPermissionId(permissionId);

                    rolePermissionRepository.save(rolePermission);
                }
            }
        }

        return null;
    }


    public Map<Role, List<Permission>> getRolesPermissions() {
        Map<Role, List<Permission>> result = new LinkedHashMap<>();

        List<Role> roles = roleRepository.findAll();

        for (Role role : roles) {
            List<Long> permissionIds = rolePermissionRepository.findByRoleId(role.getId())
                    .stream()
                    .map(RolePermission::getPermissionId)
                    .toList();

            List<Permission> permissions = permissionRepository.findAllById(permissionIds);

            result.put(role, permissions);
        }

        return result;
    }
}