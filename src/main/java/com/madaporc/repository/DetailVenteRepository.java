package com.madaporc.repository;

import com.madaporc.model.DetailVente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetailVenteRepository extends JpaRepository<DetailVente, Long> {
    List<DetailVente> findByVenteId(Long venteId);
}