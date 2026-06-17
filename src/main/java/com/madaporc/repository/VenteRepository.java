package com.madaporc.repository;

import com.madaporc.DTO.DetailFinancierDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VenteRepository extends JpaRepository<Vente, Integer> {

    @Query("SELECT COALESCE(SUM(v.montantTotal), 0) FROM Vente v " +
           "WHERE v.dateVente BETWEEN :debut AND :fin AND v.statutVente = 'Validee'")
    BigDecimal sumMontantByIdDateBetween(
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin);

    // Projection DTO pour éviter d'utiliser l'entité directement
    @Query("SELECT new com.madaporc.DTO.DetailFinancierDTO(" +
           "v.dateVente, " +
           "CONCAT('Vente #', v.id), " +
           "v.montantTotal, " +
           "'Vente') " +
           "FROM Vente v " +
           "WHERE v.dateVente BETWEEN :debut AND :fin AND v.statutVente = 'Validee'")
    List<DetailFinancierDTO> findDetailsByDateVenteBetween(
            @Param("debut") LocalDateTime debut,
            @Param("fin") LocalDateTime fin);
}