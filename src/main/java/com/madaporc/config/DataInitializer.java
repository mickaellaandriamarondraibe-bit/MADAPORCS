package com.madaporc.config;

import com.madaporc.model.*;
import com.madaporc.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final StatutUtilisateurRepository statutUtilisateurRepository;

    public DataInitializer(UtilisateurRepository utilisateurRepository,
                           RoleRepository roleRepository,
                           PermissionRepository permissionRepository,
                           StatutUtilisateurRepository statutUtilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.statutUtilisateurRepository = statutUtilisateurRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Créer des statuts utilisateur
        StatutUtilisateur actif = new StatutUtilisateur();
        actif.setNom("Actif");
        actif.setDescription("Utilisateur actif et peut se connecter");
        statutUtilisateurRepository.save(actif);

        StatutUtilisateur inactif = new StatutUtilisateur();
        inactif.setNom("Inactif");
        inactif.setDescription("Utilisateur désactivé, ne peut pas se connecter");
        statutUtilisateurRepository.save(inactif);

        // Créer des permissions
        Permission userRead = new Permission();
        userRead.setNom("Lire utilisateurs");
        userRead.setCode("USER_READ");
        userRead.setDescription("Permet de consulter la liste des utilisateurs");
        permissionRepository.save(userRead);

        Permission userWrite = new Permission();
        userWrite.setNom("Écrire utilisateurs");
        userWrite.setCode("USER_WRITE");
        userWrite.setDescription("Permet de créer et modifier des utilisateurs");
        permissionRepository.save(userWrite);

        Permission userDelete = new Permission();
        userDelete.setNom("Supprimer utilisateurs");
        userDelete.setCode("USER_DELETE");
        userDelete.setDescription("Permet de désactiver/supprimer des utilisateurs");
        permissionRepository.save(userDelete);

        // Créer des rôles
        Role admin = new Role();
        admin.setNom("Administrateur");
        admin.setDescription("Accès complet à toutes les fonctionnalités");
        admin.setPermissions(Arrays.asList(userRead, userWrite, userDelete));
        roleRepository.save(admin);

        Role utilisateur = new Role();
        utilisateur.setNom("Utilisateur");
        utilisateur.setDescription("Accès de base");
        utilisateur.setPermissions(Arrays.asList(userRead));
        roleRepository.save(utilisateur);

        // Créer un utilisateur admin de test
        Utilisateur adminUser = new Utilisateur();
        adminUser.setNom("Administrateur");
        adminUser.setEmail("admin@madaporc.com");
        adminUser.setMotDePasse("admin123"); // En production, il faut hasher le mot de passe !
        adminUser.setRole(admin);
        adminUser.setStatut(actif);
        utilisateurRepository.save(adminUser);
    }
}
