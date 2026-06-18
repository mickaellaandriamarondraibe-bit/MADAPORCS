package com.madaporc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.madaporc.model.Employe;

public interface EmployeRepository extends JpaRepository<Employe, Long> {
    List<String> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    @Query("""
            SELECT e FROM Employe e
            WHERE (:motCle IS NULL OR LOWER(e.nom) LIKE LOWER(CONCAT('%', :motCle, '%'))
               OR LOWER(e.prenom) LIKE LOWER(CONCAT('%', :motCle, '%')))
            AND (:posteId IS NULL OR e.posteEmploye.id = :posteId)
            AND (:statutId IS NULL OR e.statutEmploye.id = :statutId)
            """)
    List<Employe> rechercherEmployes(
            @Param("motCle") String motCle,
            @Param("posteId") Long posteId,
            @Param("statutId") Long statutId);
}
