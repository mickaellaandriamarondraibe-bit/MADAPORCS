package com.madaporc.repository;

import com.madaporc.model.Depense;
import com.madaporc.DTO.DetailFinancierDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DepenseRepository extends JpaRepository<Depense, Integer> {

    @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Depense d " +
           "WHERE d.dateDepense BETWEEN :debut AND :fin")
    BigDecimal sumMontantByIdDateBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);

    // Projection DTO pour éviter d'utiliser l'entité directement
    @Query("SELECT new com.madaporc.DTO.DetailFinancierDTO(" +
           "d.dateDepense, " +
           "d.libelle, " +
           "d.montant, " +
           "c.libelle) " +
           "FROM Depense d " +
           "LEFT JOIN d.categorieDepense c " +
           "WHERE d.dateDepense BETWEEN :debut AND :fin")
    List<DetailFinancierDTO> findDetailsByDateDepenseBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);
}