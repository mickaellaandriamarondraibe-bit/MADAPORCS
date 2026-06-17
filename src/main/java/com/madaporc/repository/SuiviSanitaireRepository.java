package com.madaporc.repository;

import com.madaporc.DTO.DetailSanitaireDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SuiviSanitaireRepository extends JpaRepository<SuiviSanitaire, Integer> {

    // Projection DTO pour éviter d'utiliser l'entité directement
    @Query("SELECT new com.madaporc.DTO.DetailSanitaireDTO(" +
           "s.dateDiagnostic, " +
           "CASE WHEN s.lotPorc IS NOT NULL THEN CONCAT('Lot ', s.lotPorc.codeLot) " +
           "     WHEN s.reproducteur IS NOT NULL THEN CONCAT('Reproducteur ', s.reproducteur.codeReproducteur) " +
           "     ELSE 'N/A' END, " +
           "m.libelle, " +
           "s.nombrePorcsMalades, " +
           "st.libelle) " +
           "FROM SuiviSanitaire s " +
           "LEFT JOIN s.maladie m " +
           "LEFT JOIN s.statutSuiviSanitaire st " +
           "WHERE s.dateDiagnostic BETWEEN :debut AND :fin")
    List<DetailSanitaireDTO> findDetailsByDateDiagnosticBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);
    
    // Méthode pour compter les cas par statut
    @Query("SELECT COUNT(s) FROM SuiviSanitaire s " +
           "WHERE s.dateDiagnostic BETWEEN :debut AND :fin " +
           "AND s.statutSuiviSanitaire.libelle = :statut")
    Long countByDateBetweenAndStatut(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin,
            @Param("statut") String statut);
}