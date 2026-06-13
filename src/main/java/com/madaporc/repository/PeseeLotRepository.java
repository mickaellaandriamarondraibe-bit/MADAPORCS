package com.madaporc.repository;

import org.springframework.stereotype.Repository;

import com.madaporc.model.PeseeLot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository placeholder pour PeseeLot.
 * À transformer plus tard en JpaRepository<PeseeLot, Long> après création de l'Entity JPA.
 */
@Repository
public interface PeseeLotRepository extends JpaRepository<PeseeLot, Long> {
    List<PeseeLot> findByLotPorcIdOrderByDatePeseeDesc(Long lotId);
}
