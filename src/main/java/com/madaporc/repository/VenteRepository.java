package com.madaporc.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.madaporc.model.Vente;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {

  @Override
  @EntityGraph(attributePaths = {"client"})
  List<Vente> findAll();

  @EntityGraph(attributePaths = {"client"})
  List<Vente> findAllByOrderByCreatedAtDesc();

  @Override
  @EntityGraph(attributePaths = {"client", "lignes", "lignes.lot"})
  Optional<Vente> findById(Long id);

    @Query("""
            select coalesce(sum(v.montantTotal), 0)
            from Vente v
            where v.dateVente between :debut and :fin
              and v.statut = 'VALIDEE'
            """)
    BigDecimal sommeVentesValideesEntre(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    // Nombre de ventes validees sur la periode (pour le rapport financier)
    long countByDateVenteBetweenAndStatut(LocalDate debut, LocalDate fin, String statut);


    List<Vente> findByDateVenteBetween(LocalDate debut, LocalDate fin);

    List<Vente> findByStatutOrderByDateVenteDesc(String statut);

    
}
