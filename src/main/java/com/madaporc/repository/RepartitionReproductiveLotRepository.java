package com.madaporc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madaporc.model.RepartitionReproductiveLot;

public interface RepartitionReproductiveLotRepository
        extends JpaRepository<RepartitionReproductiveLot, Long> {

    List<RepartitionReproductiveLot> findByLotId(Long lotId);

}