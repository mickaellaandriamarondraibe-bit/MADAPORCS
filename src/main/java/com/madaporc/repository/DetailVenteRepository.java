package com.madaporc.repository;

<<<<<<< HEAD
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.madaporc.model.DetailVente;

@Repository
public interface DetailVenteRepository extends JpaRepository<DetailVente, Long> {

	List<DetailVente> findByVenteIdOrderByIdAsc(Long venteId);
}
=======
import com.madaporc.model.DetailVente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetailVenteRepository extends JpaRepository<DetailVente, Long> {
    List<DetailVente> findByVenteId(Long venteId);
}
>>>>>>> 5e4d8bf (correction)
