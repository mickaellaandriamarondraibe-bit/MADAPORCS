package com.madaporc.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.madaporc.model.Vente;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {

    @Query("""
            select coalesce(sum(v.montantTotal), 0)
            from Vente v
            where v.dateVente between :debut and :fin
              and v.statut = 'VALIDEE'
            """)
    BigDecimal sommeVentesValideesEntre(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    // Nombre de ventes validees sur la periode (pour le rapport financier)
    long countByDateVenteBetweenAndStatut(LocalDate debut, LocalDate fin, String statut);
}
