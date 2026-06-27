package com.madaporc.repository;

import org.springframework.stereotype.Repository;

import com.madaporc.model.MouvementStockAliment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStockAliment, Long> {
    List<MouvementStockAliment> findAllByOrderDateMouvementDesc();
    List<MouvementStockAliment> findByIngredientIdOrderByDateMouvementDesc(Long ingredientId);
}
