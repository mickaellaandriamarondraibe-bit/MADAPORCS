package com.madaporc.repository;

import com.madaporc.model.LotPorc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LotPorcRepository extends JpaRepository<LotPorc, Long> {

    boolean existsByCodeLot(String codeLot);

    List<LotPorc> findByCodeLotContainingIgnoreCase(String codeLot);

    List<LotPorc> findBySexe(String sexe);

    List<LotPorc> findByObjectif(String objectif);

    List<LotPorc> findByStatut(String statut);
    Optional<LotPorc> findByCodeLot(String codeLot);
    List<LotPorc> findByDateCreationBetween(LocalDate startDate, LocalDate endDate);
    List<LotPorc> findBySexeAndStatut(String Sexe,String status);
    long countByStatut(String statut);
}