package com.madaporc.repository;

import com.madaporc.dto.GroupeReproductionDTO;
import com.madaporc.model.GroupeReproduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GroupeReproductionRepository extends JpaRepository<GroupeReproduction, Long> {

    Optional<GroupeReproduction> findByCodeGroupe(String codeGroupe);

    boolean existsByCodeGroupe(String codeGroupe);

    List<GroupeReproduction> findByStatutOrderByDateSaillieDesc(String statut);

    List<GroupeReproduction> findByLotFemelleIdOrderByDateSaillieDesc(Long lotFemelleId);

    List<GroupeReproduction> findByLotMaleIdOrderByDateSaillieDesc(Long lotMaleId);

    List<GroupeReproduction> findByLotFemelleId(Long lotFemelleId);
    
    List<GroupeReproduction> findAllByOrderByDateSaillieDesc();

    List<GroupeReproduction> findByDatePrevueMiseBasBetweenAndDateMiseBasReelleIsNullOrderByDatePrevueMiseBasAsc(
            LocalDate debut,
            LocalDate fin);
    long countByStatutIn(List<String> statuts);
}