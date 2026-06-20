package com.madaporc.repository;

import com.madaporc.model.MouvementLotPorc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MouvementLotPorcRepository extends JpaRepository<MouvementLotPorc, Long> {
    
}