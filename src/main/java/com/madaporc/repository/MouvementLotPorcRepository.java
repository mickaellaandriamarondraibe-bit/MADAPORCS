package com.madaporc.repository;

import com.madaporc.model.MouvementLotPorc;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository placeholder pour MouvementLotPorc.
 * À transformer plus tard en JpaRepository<MouvementLotPorc, Long> après création de l'Entity JPA.
 */
@Repository
public interface MouvementLotPorcRepository extends JpaRepository<MouvementLotPorc, Long> {
    List<MouvementLotPorc> findByLotPorcIdOrderByDateMouvementDesc(Long lotPorcId);
}