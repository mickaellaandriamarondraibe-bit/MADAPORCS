package com.madaporc.repository;

import com.madaporc.model.AlerteReproduction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlerteReproductionRepository extends JpaRepository<AlerteReproduction, Integer> {
 List<AlerteReproduction> findByStatut(String statut);
List<AlerteReproduction> findByTypeAlerteAndStatut(String typeAlerte, String
statut);
 
}