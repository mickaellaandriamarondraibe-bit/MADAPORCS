package com.madaporc.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madaporc.DTO.RolePermissionDTO;
import com.madaporc.DTO.UtilisateurDTO;
import com.madaporc.model.Permission;
import com.madaporc.model.Role;
import com.madaporc.model.RolePermission;
import com.madaporc.model.Utilisateur;
import com.madaporc.repository.PermissionRepository;
import com.madaporc.repository.RolePermissionRepository;
import com.madaporc.repository.RoleRepository;
import com.madaporc.repository.StatutUtilisateurRepository;
import com.madaporc.repository.UtilisateurRepository;

@Service
public class UtilisateurService {

    private static final Long STATUT_ACTIF_ID = 1L;
    private static final Long STATUT_INACTIF_ID = 2L;

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final StatutUtilisateurRepository statutUtilisateurRepository;

    public UtilisateurService(
            UtilisateurRepository utilisateurRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository,
            StatutUtilisateurRepository statutUtilisateurRepository
    ) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.statutUtilisateurRepository = statutUtilisateurRepository;
    }


    public String verifierPermissionAdmin(Long utilisateurId) {
        if (utilisateurId == null) {
            return "Utilisateur non connecté.";
        }

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId).orElse(null);

        if (utilisateur == null) {
            return "Utilisateur introuvable.";
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


    public List<Utilisateur> findAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }


    public String creer(UtilisateurDTO dto) {
        String erreur = validerUtilisateur(dto, null, true);

        if (erreur != null) {
            return erreur;
        }

        Utilisateur utilisateur = convertirDtoVersEntity(dto);

        utilisateur.setMotDePasseHash(hasherMotDePasse(dto.getMotDePasse()));
        utilisateur.setCreatedAt(LocalDateTime.now());
        utilisateur.setUpdatedAt(LocalDateTime.now());

        if (utilisateur.getStatutUtilisateurId() == null) {
            utilisateur.setStatutUtilisateurId(STATUT_ACTIF_ID);
        }

        utilisateurRepository.save(utilisateur);

        return null;
    }


    public String modifier(Long id, UtilisateurDTO dto) {
        if (id == null) {
            return "Identifiant utilisateur invalide.";
        }

        Utilisateur utilisateur = utilisateurRepository.findById(id).orElse(null);

        if (utilisateur == null) {
            return "Utilisateur introuvable.";
        }

        String erreur = validerUtilisateur(dto, id, false);

        if (erreur != null) {
            return erreur;
        }

        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setRoleId(dto.getRoleId());
        utilisateur.setStatutUtilisateurId(dto.getStatutUtilisateurId());
        utilisateur.setUpdatedAt(LocalDateTime.now());

        utilisateurRepository.save(utilisateur);

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


    public String validerUtilisateur(UtilisateurDTO dto, Long idActuel, boolean creation) {
        if (dto == null) {
            return "Utilisateur obligatoire.";
        }

        if (dto.getNom() == null || dto.getNom().isBlank()) {
            return "Nom obligatoire.";
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return "Email obligatoire.";
        }

        if (!dto.getEmail().contains("@")) {
            return "Email invalide.";
        }

        if (dto.getRoleId() == null) {
            return "Rôle obligatoire.";
        }

        if (!roleRepository.existsById(dto.getRoleId())) {
            return "Rôle introuvable.";
        }

        if (dto.getStatutUtilisateurId() != null
                && !statutUtilisateurRepository.existsById(dto.getStatutUtilisateurId())) {
            return "Statut utilisateur introuvable.";
        }

        Optional<Utilisateur> emailExistant = utilisateurRepository.findByEmail(dto.getEmail());

        if (emailExistant.isPresent()
                && (idActuel == null || !emailExistant.get().getId().equals(idActuel))) {
            return "Cet email est déjà utilisé.";
        }

        if (creation) {
            if (dto.getMotDePasse() == null || dto.getMotDePasse().isBlank()) {
                return "Mot de passe obligatoire.";
            }

            if (dto.getMotDePasse().length() < 6) {
                return "Le mot de passe doit contenir au moins 6 caractères.";
            }
        }

        return null;
    }


    public Utilisateur convertirDtoVersEntity(UtilisateurDTO dto) {
        Utilisateur utilisateur = new Utilisateur();

        utilisateur.setId(dto.getId());
        utilisateur.setNom(dto.getNom());
        utilisateur.setEmail(dto.getEmail());
        utilisateur.setRoleId(dto.getRoleId());
        utilisateur.setStatutUtilisateurId(dto.getStatutUtilisateurId());

        return utilisateur;
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