package com.madaporc.repository;

    import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
    import com.madaporc.model.LotPorc;

    public interface LotPorcRepository extends JpaRepository<LotPorc, Long> {

        List<LotPorc> findByCodeLotContainingIgnoreCase(String codeLot);

    List<LotPorc> findByRaceIdAndStatutLotId(Long raceId, Long statutLotId);

    List<LotPorc> findByStatutLotId(Long statutLotId);

    Optional<LotPorc> findByCodeLot(String codeLot);

    boolean existsByCodeLot(String codeLot);

    long countByStatutLotId(Long statutLotId);

    @Query("select coalesce(sum(l.nombreActuel),0) from LotPorc l where l.archivedAt is null")
    Integer sumNombreActuelActifs();
    }
