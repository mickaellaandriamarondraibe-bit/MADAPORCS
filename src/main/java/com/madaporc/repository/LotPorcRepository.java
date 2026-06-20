package com.madaporc.repository;

import com.madaporc.model.LotPorc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotPorcRepository extends JpaRepository<LotPorc, Long> {
    
}