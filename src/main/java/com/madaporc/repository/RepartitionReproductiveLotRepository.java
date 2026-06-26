package com.madaporc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.madaporc.model.RepartitionReproductiveLot;

public interface RepartitionReproductiveLotRepository
        extends JpaRepository<RepartitionReproductiveLot, Long> {

    List<RepartitionReproductiveLot> findByLotId(Long lotId);

    Optional<RepartitionReproductiveLot> findByLotIdAndStatutReproductif(
            Long lotId,
            String statutReproductif
    );

    @Query("""
        SELECT COALESCE(SUM(r.quantite), 0)
        FROM RepartitionReproductiveLot r
        WHERE r.lot.id = :lotId
        AND r.statutReproductif = :statutCode
    """)
    Integer findQuantiteByLotIdAndStatutCode(
            @Param("lotId") Long lotId,
            @Param("statutCode") String statutCode
    );
}