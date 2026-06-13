package com.madaporc.repository;

import com.madaporc.model.Vente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {

    List<Vente> findByClient_IdOrderByDateVenteDesc(Long clientId);
}
