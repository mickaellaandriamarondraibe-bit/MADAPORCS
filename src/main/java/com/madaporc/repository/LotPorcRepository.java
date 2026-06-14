package com.madaporc.repository;

import com.madaporc.model.LotPorc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotPorcRepository extends JpaRepository<LotPorc, Long> {

    List<LotPorc> findByCodeLotContainingIgnoreCase(String code);

    List<LotPorc> findByRaceIdAndStatutLotId(Long raceId, Long statutLotId);

    Optional<LotPorc> findById(Long id);

    boolean existsByCodeLot(String codeLot);

    long countByStatutLotId(Long statutLotId);

    List<LotPorc> findByArchivedAtIsNull();

    List<LotPorc> findByArchivedAtIsNullAndCodeLotContainingIgnoreCase(String code);

    List<LotPorc> findByArchivedAtIsNullAndRaceIdAndStatutLotId(Long raceId, Long statutLotId);
}