package com.madaporc.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

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
}
