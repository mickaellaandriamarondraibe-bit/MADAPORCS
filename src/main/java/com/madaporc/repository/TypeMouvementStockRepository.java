package com.madaporc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.madaporc.model.TypeMouvementStock;

@Repository
public interface TypeMouvementStockRepository extends JpaRepository<TypeMouvementStock, Long> {
    Optional<TypeMouvementStock> findByLibelle(String libelle);

}