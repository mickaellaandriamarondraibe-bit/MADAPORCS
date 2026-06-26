package com.madaporc.repository;

import com.madaporc.model.PeseeLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeseeLotRepository extends JpaRepository<PeseeLot, Long> {

    List<PeseeLot> findByLotIdOrderByDatePeseeAsc(Long lotId);
}