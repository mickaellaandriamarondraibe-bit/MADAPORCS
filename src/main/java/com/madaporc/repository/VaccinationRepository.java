package com.madaporc.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.madaporc.model.Vaccination;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

    List<Vaccination> findByDateRappelBetweenOrderByDateRappelAsc(LocalDate debut, LocalDate fin);
}
