package com.madaporc.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.madaporc.model.RepartitionReproductiveLot;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface RepartitionReproductiveLotRepository extends JpaRepository<RepartitionReproductiveLot, Long> {
    List<RepartitionReproductiveLot> findByLotId(Long lotId);

    Optional<RepartitionReproductiveLot> findByLotIdAndStatutReproductif(Long lotId, String statutReproductif);

    @Query("""
            SELECT quantite FROM RepartitionReproductiveLot r
            WHERE r.lotPorc.id = :lotId AND r.statutReproductif.code = :statutCode
            """)Integer findQuantiteByLotIdAndStatutCode(@Param("lotId") Long lotId, @Param("statutCode") String statutCode);
}