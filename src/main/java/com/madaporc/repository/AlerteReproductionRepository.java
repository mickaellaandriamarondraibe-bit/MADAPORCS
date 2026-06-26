package com.madaporc.repository;

import com.madaporc.model.AlerteReproduction;
import com.madaporc.model.AlerteReproduction.StatutAlerte;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlerteReproductionRepository extends JpaRepository<AlerteReproduction, Long> {

    List<AlerteReproduction> findByStatutOrderByDateAlerteDesc(StatutAlerte statut);

    List<AlerteReproduction> findByStatutInOrderByDateAlerteDesc(Collection<StatutAlerte> statuts);

    boolean existsByGroupeReproductionIdAndTypeAlerteAndStatutIn(
            Long groupeReproductionId,
            String typeAlerte,
            Collection<StatutAlerte> statuts);
}
