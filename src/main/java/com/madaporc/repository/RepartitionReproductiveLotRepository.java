package com.madaporc.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madaporc.model.RepartitionReproductiveLot;

public interface RepartitionReproductiveLotRepository
        extends JpaRepository<RepartitionReproductiveLot, Long> {

    List<RepartitionReproductiveLot> findByLotId(Long lotId);

    Optional<RepartitionReproductiveLot> findByLotIdAndStatutReproductif(Long lotId, String statutReproductif);

}
