package com.madaporc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.madaporc.model.AnalyseReproductionLot;

public interface AnalyseReproductionLotRepository
        extends JpaRepository<AnalyseReproductionLot, Long> {

    @Query("""
        SELECT a
        FROM AnalyseReproductionLot a
        WHERE a.lotPorc.id = :lotId
        ORDER BY a.dateAnalyse DESC
    """)
    List<AnalyseReproductionLot> findByLotIdOrderByDateAnalyseDesc(
            @Param("lotId") Long lotId
    );
}