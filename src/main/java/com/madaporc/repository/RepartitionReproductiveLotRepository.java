package com.madaporc.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.madaporc.model.RepartitionReproductiveLot;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepartitionReproductiveLotRepository extends JpaRepository<RepartitionReproductiveLot, Long> {
    List<RepartitionReproductiveLot> findByLotId(Long lotId);

    Optional<RepartitionReproductiveLot> findByLotIdAndStatutReproductif(Long lotId, String statutReproductif);
}