package com.madaporc.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.madaporc.model.Paiement;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByVenteId(Long venteId);

    @Query("select coalesce(sum(p.montant), 0) from Paiement p where p.venteId = :venteId")
    BigDecimal sumMontantByVenteId(@Param("venteId") Long venteId);
}