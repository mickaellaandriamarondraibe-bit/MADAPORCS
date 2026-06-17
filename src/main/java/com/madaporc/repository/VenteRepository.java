package com.madaporc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface VenteRepository extends JpaRepository<Vente, Integer> {

    @Query("SELECT COALESCE(SUM(v.montantTotal), 0) FROM Vente v " +
           "WHERE v.dateVente BETWEEN :debut AND :fin AND v.statutVente = 'Validee'")
    BigDecimal sumMontantByIdDateBetween(
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin);
}