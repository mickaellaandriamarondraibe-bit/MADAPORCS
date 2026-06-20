package com.madaporc.repository;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.madaporc.model.AnalyseReproductionLot;

import java.util.List;

@Repository
public interface AnalyseReproductionLotRepository extends JpaRepository<AnalyseReproductionLot, Long> { 
    List<AnalyseReproductionLot> findByLotIdOrderByDateAnalyseDesc(Long lotId);   
}