package com.madaporc.repository;

import com.madaporc.DTO.DetailStockDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    // Projection DTO pour éviter d'utiliser l'entité directement
    @Query("SELECT new com.madaporc.DTO.DetailStockDTO(" +
           "i.libelle, " +
           "i.stockActuelKg, " +
           "i.seuilMinKg, " +
           "i.prixKg, " +
           "CASE WHEN i.stockActuelKg < i.seuilMinKg THEN true ELSE false END) " +
           "FROM Ingredient i " +
           "WHERE i.actif = true")
    List<DetailStockDTO> findActiveIngredientsDetails();
    
    // Méthode pour compter les ingrédients sous seuil
    @Query("SELECT COUNT(i) FROM Ingredient i " +
           "WHERE i.actif = true AND i.stockActuelKg < i.seuilMinKg")
    Long countIngredientsUnderThreshold();
}