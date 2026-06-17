package com.madaporc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface CycleProductionRepository extends JpaRepository<CycleProduction, Integer> {

    @Query("SELECT COALESCE(SUM(c.nombreNaissances), 0) FROM CycleProduction c " +
           "WHERE c.dateDebut BETWEEN :debut AND :fin " +
           "OR (c.dateFinReelle IS NOT NULL AND c.dateFinReelle BETWEEN :debut AND :fin)")
    Integer sumNaissancesByDateBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);

    @Query("SELECT COALESCE(SUM(c.nombrePertes), 0) FROM CycleProduction c " +
           "WHERE c.dateDebut BETWEEN :debut AND :fin " +
           "OR (c.dateFinReelle IS NOT NULL AND c.dateFinReelle BETWEEN :debut AND :fin)")
    Integer sumPertesByDateBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);

    @Query("SELECT COALESCE(SUM(c.nombreVivants), 0) FROM CycleProduction c " +
           "WHERE c.dateDebut BETWEEN :debut AND :fin " +
           "OR (c.dateFinReelle IS NOT NULL AND c.dateFinReelle BETWEEN :debut AND :fin)")
    Integer sumVivantsByDateBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);

    @Query("SELECT COALESCE(SUM(c.nombreVendables), 0) FROM CycleProduction c " +
           "WHERE c.dateDebut BETWEEN :debut AND :fin " +
           "OR (c.dateFinReelle IS NOT NULL AND c.dateFinReelle BETWEEN :debut AND :fin)")
    Integer sumVendablesByDateBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);
}