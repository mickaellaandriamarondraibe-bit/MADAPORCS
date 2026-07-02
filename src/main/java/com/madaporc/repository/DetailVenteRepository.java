package com.madaporc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.madaporc.model.DetailVente;

@Repository
public interface DetailVenteRepository extends JpaRepository<DetailVente, Long> {

	List<DetailVente> findByVenteIdOrderByIdAsc(Long venteId);
}
