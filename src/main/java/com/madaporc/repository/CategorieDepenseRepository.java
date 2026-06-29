package com.madaporc.repository;

import com.madaporc.model.CategorieDepense;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieDepenseRepository extends JpaRepository<CategorieDepense, Long> {

    List<CategorieDepense> findAllByOrderByNomAsc();

    boolean existsByNomIgnoreCase(String nom);
}
