package com.madaporc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madaporc.model.Traitement;

public interface TraitementRepository extends JpaRepository<Traitement, Long> {

    List<Traitement> findByMaladieId(Long maladieId);

    List<Traitement> findByLibelleContainingIgnoreCase(String libelle);
}