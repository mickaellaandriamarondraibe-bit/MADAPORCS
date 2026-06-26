package com.madaporc.repository;

import com.madaporc.model.SuiviSanitaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuiviSanitaireRepository extends JpaRepository<SuiviSanitaire, Long> {

    @Query("""
        SELECT s
        FROM SuiviSanitaire s
        JOIN FETCH s.lot
        LEFT JOIN FETCH s.maladie
        LEFT JOIN FETCH s.traitement
        ORDER BY s.dateDiagnostic DESC
    """)
    List<SuiviSanitaire> findAllWithDetails();
}