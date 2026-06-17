package com.madaporc.repository;

import com.madaporc.DTO.DetailPresenceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PresenceRepository extends JpaRepository<Presence, Integer> {

    // Projection DTO pour éviter d'utiliser l'entité directement
    @Query("SELECT new com.madaporc.DTO.DetailPresenceDTO(" +
           "CONCAT(e.nom, ' ', COALESCE(e.prenom, '')), " +
           "p.datePresence, " +
           "p.statutPresence, " +
           "p.heureArrivee, " +
           "p.heureDepart) " +
           "FROM Presence p " +
           "JOIN p.employe e " +
           "WHERE p.datePresence BETWEEN :debut AND :fin")
    List<DetailPresenceDTO> findDetailsByDatePresenceBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);
    
    // Méthode pour compter les présences par statut
    @Query("SELECT COUNT(p) FROM Presence p " +
           "WHERE p.datePresence BETWEEN :debut AND :fin " +
           "AND p.statutPresence = :statut")
    Long countByDateBetweenAndStatut(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin,
            @Param("statut") String statut);
}