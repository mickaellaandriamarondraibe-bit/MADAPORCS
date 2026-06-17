package com.madaporc.repository;

import com.madaporc.DTO.DetailProductionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CycleProductionRepository extends JpaRepository<CycleProduction, Integer> {

    // Projection DTO pour éviter d'utiliser l'entité directement
    @Query("SELECT new com.madaporc.DTO.DetailProductionDTO(" +
           "c.codeCycle, " +
           "c.lotPorc.codeLot, " +
           "c.dateDebut, " +
           "c.dateFinReelle, " +
           "c.nombreNaissances, " +
           "c.nombrePertes, " +
           "c.nombreVivants, " +
           "c.nombreVendables, " +
           "c.statutCycle) " +
           "FROM CycleProduction c " +
           "WHERE c.dateDebut BETWEEN :debut AND :fin " +
           "OR (c.dateFinReelle IS NOT NULL AND c.dateFinReelle BETWEEN :debut AND :fin)")
    List<DetailProductionDTO> findDetailsByDateBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);
}