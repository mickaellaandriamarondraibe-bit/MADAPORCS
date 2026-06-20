package com.madaporc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madaporc.model.GroupeReproduction;

public interface  RepartitionReproductiveLotRepository extends JpaRepository<GroupeReproduction, Integer> {
    
}
