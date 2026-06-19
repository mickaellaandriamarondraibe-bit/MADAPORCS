package com.madaporc.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.madaporc.model.TypeMouvementLot;

public interface TypeMouvementLotRepository extends JpaRepository<TypeMouvementLot, Long> {

    Optional<TypeMouvementLot> findByLibelleIgnoreCase(String libelle);

}
