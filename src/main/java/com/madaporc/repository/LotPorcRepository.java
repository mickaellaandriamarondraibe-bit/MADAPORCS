package com.madaporc.repository;

import com.madaporc.model.LotPorc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDate;

public interface LotPorcRepository extends JpaRepository<LotPorc, Long> {
    boolean existsByCodeLot(String codeLot);

    List<LotPorc> findByCodeLotContainingIgnoreCase(String codeLot);

    List<LotPorc> findBySexe(String sexe);

    List<LotPorc> findByObjectif(String objectif);

    List<LotPorc> findByStatut(String statut);

    List<LotPorc> findByDateCreationBetween(LocalDate startDate, LocalDate endDate);

    void updateById(Long id, String codeLot, LocalDate dateCreation, String sexe, String objectif);
    
    long countByStatut(String statut);

    void updateByStatut(Long id, String statut);
}