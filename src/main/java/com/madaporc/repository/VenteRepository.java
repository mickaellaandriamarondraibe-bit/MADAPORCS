package com.madaporc.repository;

import com.madaporc.model.Vente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.*;

public interface VenteRepository extends JpaRepository<Vente, Long> {
    List<Vente> findAllByOrderByDateVenteDesc();

    List<Vente> findByDateVenteBetween(LocalDate debut, LocalDate fin);

    List<Vente> findByStatutOrderByDateVenteDesc(String statut);

    
}