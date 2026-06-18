package com.madaporc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madaporc.model.StatutEmploye;

public interface StatutEmployeRepository extends JpaRepository<StatutEmploye, Long> {
    Optional<StatutEmploye> findByLibelleIgnoreCase(String libelle);
}
