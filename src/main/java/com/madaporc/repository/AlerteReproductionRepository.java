package com.madaporc.repository;

import com.madaporc.model.AlerteReproduction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlerteReproductionRepository extends JpaRepository<AlerteReproduction, Integer> {
}