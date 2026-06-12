package com.madaporc.repository;

import com.madaporc.model.Traitement;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository pour la gestion des traitements.
 */
@Repository
public interface TraitementRepository extends JpaRepository<Traitement, Long> {
    
    List<Traitement> findByMaladieId(Long maladieId);
    
    List<Traitement> findByActifTrue();
    
    @Query("SELECT t FROM Traitement t WHERE t.actif = true AND (LOWER(t.libelle) LIKE LOWER(CONCAT('%', :motCle, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :motCle, '%')))") 
    List<Traitement> rechercherParMotCle(@Param("motCle") String motCle);
}
