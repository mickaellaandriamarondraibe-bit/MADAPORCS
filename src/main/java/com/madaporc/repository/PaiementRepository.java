package com.madaporc.repository;

import com.madaporc.model.Paiement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    List<Paiement> findByVente_IdOrderByDatePaiementDesc(Long venteId);
}
