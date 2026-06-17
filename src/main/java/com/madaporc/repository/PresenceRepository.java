package com.madaporc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface PresenceRepository extends JpaRepository<Presence, Integer> {

    @Query("SELECT COUNT(p) FROM Presence p " +
           "WHERE p.datePresence BETWEEN :debut AND :fin " +
           "AND p.statutPresence = :statut")
    Long countByDateBetweenAndStatut(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin,
            @Param("statut") String statut);
}