package com.madaporc.repository;

import com.madaporc.model.Maladie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository pour la gestion des maladies.
 */
@Repository
public interface MaladieRepository extends JpaRepository<Maladie, Long> {
    
    Optional<Maladie> findByLibelle(String libelle);
    
    List<Maladie> findByActifTrue();
    
    @Query("SELECT m FROM Maladie m WHERE m.actif = true AND (LOWER(m.libelle) LIKE LOWER(CONCAT('%', :motCle, '%')) OR LOWER(m.description) LIKE LOWER(CONCAT('%', :motCle, '%')))") 
    List<Maladie> rechercherParMotCle(@Param("motCle") String motCle);
    
    @Query("SELECT m FROM Maladie m WHERE m.contagieux = true AND m.actif = true")
    List<Maladie> findMaladiesContagieuses();
}
