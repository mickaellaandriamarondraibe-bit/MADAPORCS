package com.madaporc.repository;

import com.madaporc.model.Traitement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraitementRepository extends JpaRepository<Traitement, Long> {

    List<Traitement> findByMaladieId(Long maladieId);

    boolean existsByNomIgnoreCaseAndMaladieId(String nom, Long maladieId);

    boolean existsByNomIgnoreCaseAndMaladieIdAndIdNot(String nom, Long maladieId, Long id);
}