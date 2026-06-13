package com.madaporc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.madaporc.model.MouvementStockAliment;

@Repository
public interface MouvementStockAlimentRepository extends JpaRepository<MouvementStockAliment, Long> {

}