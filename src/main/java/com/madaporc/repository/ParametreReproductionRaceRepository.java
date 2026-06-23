package com.madaporc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madaporc.model.ParametreReproductionRace;

public interface ParametreReproductionRaceRepository
        extends JpaRepository<ParametreReproductionRace, Long> {

    Optional<ParametreReproductionRace> findByRaceId(Long raceId);

}