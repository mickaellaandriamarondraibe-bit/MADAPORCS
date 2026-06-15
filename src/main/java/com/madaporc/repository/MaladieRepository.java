package com.madaporc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madaporc.model.Maladie;

public interface MaladieRepository extends JpaRepository<Maladie, Long> {


    List<Maladie> findByLibelleContainingIgnoreCase(String libelle);
}   