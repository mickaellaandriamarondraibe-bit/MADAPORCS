package com.madaporc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DepenseRepository extends JpaRepository<Depense, Integer> {

    @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Depense d " +
           "WHERE d.dateDepense BETWEEN :debut AND :fin")
    BigDecimal sumMontantByIdDateBetween(
            @Param("debut") LocalDate debut,
            @Param("fin") LocalDate fin);
}