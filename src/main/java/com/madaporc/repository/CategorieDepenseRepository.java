package com.madaporc.repository;

import com.madaporc.model.CategorieDepense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieDepenseRepository extends JpaRepository<CategorieDepense, Long> {

    List<CategorieDepense> findAllByOrderByNomAsc();

    boolean existsByNomIgnoreCase(String nom);

    Optional<CategorieDepense> findFirstByNomIgnoreCase(String nom);
}