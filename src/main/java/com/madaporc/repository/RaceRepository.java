package com.madaporc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.madaporc.model.Race;

/**
 * Repository placeholder pour Race.
 * À transformer plus tard en JpaRepository<Race, Long> après création de l'Entity JPA.
 */
@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {
}
