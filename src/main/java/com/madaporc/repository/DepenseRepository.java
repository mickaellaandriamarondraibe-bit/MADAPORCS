package com.madaporc.repository;

import com.madaporc.model.Depense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {

    List<Depense> findAllByOrderByDateDepenseDesc();

    List<Depense> findByDateDepenseBetween(LocalDate debut, LocalDate fin);

    List<Depense> findByCategorieId(Long categorieId);

    @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Depense d")
    BigDecimal totalDepenses();

    @Query("""
            SELECT COALESCE(SUM(d.montant), 0)
            FROM Depense d
            WHERE d.dateDepense BETWEEN :debut AND :fin
            """)
    BigDecimal totalDepensesEntre(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
}
