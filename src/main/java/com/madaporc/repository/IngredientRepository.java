package com.madaporc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.madaporc.model.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    @Query("select i from Ingredient i where i.stockActuel <= i.seuilAlerte order by i.nom asc")
    List<Ingredient> findStocksFaibles();
    List<Ingredient> findAllByOrderByNomAsc();
    
    Ingredient findFirstByOrderByCreatedAtDesc();

    boolean existsByNomIgnoreCase(String nom);

    boolean existsByNomIgnoreCaseAndIdNot(String nom, Long id);
}
