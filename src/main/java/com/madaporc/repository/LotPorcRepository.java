package com.madaporc.repository;

import com.madaporc.model.LotPorc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LotPorcRepository extends JpaRepository<LotPorc, Long> {
    boolean existsByCodeLot(String codeLot);

    List<LotPorc> findByCodeLotContainingIgnoreCase(String codeLot);

    List<LotPorc> findBySexe(String sexe);

    List<LotPorc> findByObjectif(String objectif);

    List<LotPorc> findByStatut(String statut);
    
    long countByStatut(String statut);
}