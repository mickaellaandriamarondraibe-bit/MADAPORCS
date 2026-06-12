package com.madaporc.repository;

import com.madaporc.model.LotPorc;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository placeholder pour LotPorc.
 * À transformer plus tard en JpaRepository<LotPorc, Long> après création de l'Entity JPA.
 */
@Repository
public interface LotPorcRepository extends JpaRepository<LotPorc, Long> {
}
