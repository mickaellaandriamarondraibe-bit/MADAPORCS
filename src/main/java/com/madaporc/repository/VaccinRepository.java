package com.madaporc.repository;

import com.madaporc.model.Vaccin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinRepository extends JpaRepository<Vaccin, Long> {
}

