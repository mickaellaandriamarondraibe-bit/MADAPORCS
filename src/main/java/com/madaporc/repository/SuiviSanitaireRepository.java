package com.madaporc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SuiviSanitaireRepository extends JpaRepository<SuiviSanitaire, Integer> {

    @Query("SELECT COUNT(s) FROM SuiviSanitaire s " +
           "WHERE s.dateDiagnostic BETWEEN :debut AND :fin")
    Long countByDateBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);
    
    @Query("SELECT COUNT(s) FROM SuiviSanitaire s " +
           "WHERE s.dateDiagnostic BETWEEN :debut AND :fin " +
           "AND s.statutSuiviSanitaire.libelle = :statut")
    Long countByDateBetweenAndStatut(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin,
            @Param("statut") String statut);
}