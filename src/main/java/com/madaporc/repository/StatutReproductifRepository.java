package com.madaporc.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.madaporc.model.StatutReproductif;
import java.util.List;

@Repository
public interface StatutReproductifRepository extends JpaRepository<StatutReproductif, Long> {
    List<StatutReproductif> findAll();
}