package com.madaporc.repository;

import com.madaporc.model.DestinataireAlerte;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DestinataireAlerteRepository extends JpaRepository<DestinataireAlerte, Long> {
    boolean existsByEmail(String email);
}
