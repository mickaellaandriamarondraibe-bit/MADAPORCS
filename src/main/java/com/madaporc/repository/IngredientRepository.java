package com.madaporc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IngredientRepository extends JpaRepository<Ingredient, Integer> {

    @Query("SELECT COUNT(i) FROM Ingredient i " +
           "WHERE i.actif = true AND i.stockActuelKg < i.seuilMinKg")
    Long countIngredientsUnderThreshold();
}