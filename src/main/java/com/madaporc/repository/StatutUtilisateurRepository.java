package com.madaporc.repository;

import com.madaporc.model.StatutUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatutUtilisateurRepository extends JpaRepository<StatutUtilisateur, Long> {
    Optional<StatutUtilisateur> findByNom(String nom);
}
