package com.madaporc.repository;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.madaporc.model.AnalyseReproductionLot;

@Repository
public interface AnalyseReproductionLotRepository extends JpaRepository<AnalyseReproductionLot, Long> { 

    @Query("select coalesce(avg(a.tauxAptitudeGlobal), 0) from AnalyseReproductionLot a")
    Double moyenneTauxAptitudeGlobale();

    @Query("select coalesce(avg(a.tauxFertiliteObserve), 0) from AnalyseReproductionLot a")
    Double moyenneTauxFertiliteObserve();
}
