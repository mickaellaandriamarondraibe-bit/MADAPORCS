package com.madaporc.repository;

import com.madaporc.model.Vaccination;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository pour la gestion des vaccinations.
 */
@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    
    List<Vaccination> findByLotId(Long lotId);
    
    List<Vaccination> findByReproducteurId(Long reproducteurId);
    
    List<Vaccination> findByVaccinId(Long vaccinId);
    
    @Query("SELECT v FROM Vaccination v WHERE v.dateRappelPrevue <= :date AND v.statutRappel = 'EN_ATTENTE' AND v.actif = true")
    List<Vaccination> findRappelsCritiques(@Param("date") LocalDate date);
}
