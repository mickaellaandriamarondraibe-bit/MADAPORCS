package com.madaporc.repository;

import com.madaporc.model.SuiviSanitaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuiviSanitaireRepository extends JpaRepository<SuiviSanitaire, Long> {
}

