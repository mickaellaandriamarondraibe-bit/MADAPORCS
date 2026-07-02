package com.madaporc.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.madaporc.model.Depense;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {

    @Query("""
            select coalesce(sum(d.montant), 0)
            from Depense d
            where d.dateDepense between :debut and :fin
            """)
    BigDecimal sommeDepensesEntre(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    // Nombre de depenses sur la periode (pour le rapport financier)
    long countByDateDepenseBetween(LocalDate debut, LocalDate fin);
    List<Depense> findAllByOrderByDateDepenseDesc();

    List<Depense> findByDateDepenseBetween(LocalDate debut, LocalDate fin);
    
      @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Depense d")
    BigDecimal totalDepenses();

    @Query("""
            SELECT COALESCE(SUM(d.montant), 0)
            FROM Depense d
            WHERE d.dateDepense BETWEEN :debut AND :fin
            """)
    BigDecimal totalDepensesEntre(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

}
