package com.madaporc.repository;

import com.madaporc.model.Vaccin;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository pour la gestion des vaccins.
 */
@Repository
public interface VaccinRepository extends JpaRepository<Vaccin, Long> {
    
    Optional<Vaccin> findByLibelle(String libelle);
    
    List<Vaccin> findByActifTrue();
    
    @Query("SELECT v FROM Vaccin v WHERE v.actif = true AND (LOWER(v.libelle) LIKE LOWER(CONCAT('%', :motCle, '%')) OR LOWER(v.fabricant) LIKE LOWER(CONCAT('%', :motCle, '%')))") 
    List<Vaccin> rechercherParMotCle(@Param("motCle") String motCle);
}
